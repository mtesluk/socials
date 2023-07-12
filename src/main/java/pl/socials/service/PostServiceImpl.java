package pl.socials.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.socials.controller.PostController;
import pl.socials.dto.PostInDto;
import pl.socials.dto.PostOutDto;
import pl.socials.exception.InvalidRequestException;
import pl.socials.exception.NotFoundException;
import pl.socials.mapper.PostMapper;
import pl.socials.model.Post;
import pl.socials.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    public static final String EXC_MSG_POST_NOT_FOUND = "The post not found";
    public static final String EXC_MSG_POST_UPD_WRONG_DATA = "No content and author to update the posts=";

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Override
    @Transactional
    public PostOutDto getPost(Long id) {
        logger.info(String.format("PostServiceImpl:getPost START %s", id));

        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException(EXC_MSG_POST_NOT_FOUND));
        post.setViewCount(post.getViewCount() + 1);

        logger.debug(String.format("PostServiceImpl:getPost 1 %s", post));

        return postMapper.toPostDto(post);
    }

    @Override
    public List<PostOutDto> getPosts(int page, int size, String sortBy, boolean asc) {
        logger.info(String.format("PostServiceImpl:getPosts START %s %s %s %s", page, size, sortBy, asc));

        Sort sort = asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // maybe native sql would be better, to cut down unnecessary request for count
        List<Post> posts =
                postRepository.findAll(PageRequest.of(page, size, sort)).getContent();

        logger.info(String.format("PostServiceImpl:getPosts 1 %s", posts));

        return postMapper.toPostDtoList(posts);
    }

    @Override
    public PostOutDto createPost(PostInDto postInDto) {
        logger.info(String.format("PostServiceImpl:createPost START %s", postInDto.toString()));

        Post post = Post.builder()
                .author(postInDto.getAuthor())
                .content(postInDto.getContent())
                .build();
        postRepository.save(post);
        return postMapper.toPostDto(post);
    }

    @Override
    @Transactional
    public void updatePost(Long id, PostInDto postInDto) {
        logger.info(String.format("PostServiceImpl:updatePost START %s %s", id, postInDto.toString()));

        if (postInDto.getContent() == null && postInDto.getAuthor() == null)
            throw new InvalidRequestException(EXC_MSG_POST_UPD_WRONG_DATA);
        postRepository.updateContentAndAuthor(id, postInDto.getContent(), postInDto.getAuthor());
    }

    @Override
    public void deletePost(Long id) {
        logger.info(String.format("PostServiceImpl:deletePost START %s", id));

        postRepository.deleteById(id);
    }
}
