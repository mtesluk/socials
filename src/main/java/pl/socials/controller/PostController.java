package pl.socials.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.socials.dto.PostInDto;
import pl.socials.dto.PostOutDto;
import pl.socials.service.PostService;

@RestController
@RequestMapping("/api/v1/posts/")
@Tag(name = "Posts API")
public class PostController {

    @Autowired
    private PostService postService;

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping("{id}")
    @Operation(
            summary = "Get a post by id",
            description = "Returns a post by the id. Every call increase view counter by one.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
                @ApiResponse(responseCode = "404", description = "Not found - The post was not found")
            })
    public ResponseEntity<PostOutDto> getPost(@PathVariable Long id) {
        logger.info(String.format("PostController:getPost START %s", id));
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping("")
    @Operation(
            summary = "Get a list of posts",
            description =
                    "Returns a list of 10 posts. There are ordered by view count descending. Field content is shorten.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved")})
    public ResponseEntity<List<PostOutDto>> getPosts() {
        logger.info("PostController:getPosts START");
        return ResponseEntity.ok(postService.getPosts(0, 10, "viewCount", false));
    }

    @PostMapping("")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new post", description = "Create a new post and return this object")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successfully created")})
    public PostOutDto createPost(@RequestBody PostInDto postInDto) {
        logger.info(
                String.format("PostController:createPost START %s %s", postInDto.getAuthor(), postInDto.getContent()));
        return postService.createPost(postInDto);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Update an existed post",
            description = "Update a post, but it's only possibly with author and content")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "No content"),
                @ApiResponse(responseCode = "401", description = "Bad requests (wrong data)")
            })
    public void updatePost(@PathVariable Long id, @RequestBody PostInDto postInDto) {
        logger.info(String.format(
                "PostController:updatePost START %s %s %s", id, postInDto.getAuthor(), postInDto.getContent()));
        postService.updatePost(id, postInDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an existed post")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No content")})
    public void deletePost(@PathVariable Long id) {
        logger.info(String.format("PostController:deletePost START %s", id));
        postService.deletePost(id);
    }
}
