package pl.socials.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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

    public static final String EXC_MSG_POST_NOT_FOUND = "Post not found";
    public static final String EXC_MSG_POST_UPD_WRONG_DATA = "No content and author";

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public PostOutDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException(EXC_MSG_POST_NOT_FOUND));

        return postMapper.toPostDto(post);
    }

    @Override
    public List<PostOutDto> getPosts(int page, int size, String sortBy, boolean asc) {
        Sort sort = asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // maybe native sql would be better, to cut down unnecessary request for count
        List<Post> posts =
                postRepository.findAll(PageRequest.of(page, size, sort)).getContent();

        return postMapper.toPostDtoList(posts);
    }

    @Override
    public PostOutDto createPost(PostInDto postInDto) {
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
        if (postInDto == null || (postInDto.getContent() == null && postInDto.getAuthor() == null))
            throw new InvalidRequestException(EXC_MSG_POST_UPD_WRONG_DATA);
        postRepository.updateContentAndAuthor(id, postInDto.getContent(), postInDto.getAuthor());
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
