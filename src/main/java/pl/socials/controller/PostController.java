package pl.socials.controller;

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
public class PostController {

    @Autowired
    private PostService postService;

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping("{id}")
    public ResponseEntity<PostOutDto> getPost(@PathVariable Long id) {
        logger.info(String.format("PostController:getPost START %s", id));
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping("")
    public ResponseEntity<List<PostOutDto>> getPosts() {
        logger.info("PostController:getPosts START");
        return ResponseEntity.ok(postService.getPosts(0, 10, "viewCount", false));
    }

    @PostMapping("")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public PostOutDto createPost(@RequestBody PostInDto postInDto) {
        logger.info(
                String.format("PostController:createPost START %s %s", postInDto.getAuthor(), postInDto.getContent()));
        return postService.createPost(postInDto);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(@PathVariable Long id, @RequestBody PostInDto postInDto) {
        logger.info(String.format(
                "PostController:updatePost START %s %s %s", id, postInDto.getAuthor(), postInDto.getContent()));
        postService.updatePost(id, postInDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        logger.info(String.format("PostController:deletePost START %s", id));
        postService.deletePost(id);
    }
}
