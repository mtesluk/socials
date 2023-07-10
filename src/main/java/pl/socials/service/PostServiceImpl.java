package pl.socials.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.socials.dto.PostInDto;
import pl.socials.dto.PostOutDto;
import pl.socials.exception.NotFoundException;
import pl.socials.mapper.PostMapper;
import pl.socials.model.Post;
import pl.socials.repository.PostRepository;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostOutDto getPost(Long id) {
        return postRepository
                .findById(id)
                .map(postMapper::toPostDto)
                .orElseThrow(() -> new NotFoundException("Post not found"));
    }

    @Override
    public List<PostOutDto> getPosts() {
        // maybe native sql would be better, to cut down unnecessery request for count
        return postMapper.toPostDtoList(postRepository
                .findAll(PageRequest.of(0, 10, Sort.by("viewCount").descending()))
                .getContent());
    }

    @Override
    public PostOutDto createPost(PostInDto postInDto) {
        // maybe native sql would be better, to cut down unnecessery request for first getting and then updating
        Post post = Post.builder()
                .author(postInDto.getAuthor())
                .content(postInDto.getContent())
                .build();
        postRepository.save(post);
        return postMapper.toPostDto(post);
    }

    @Override
    public void updatePost(Long id, PostInDto postInDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        post.setContent(postInDto.getContent());
        postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
