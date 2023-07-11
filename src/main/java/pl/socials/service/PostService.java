package pl.socials.service;

import java.util.List;
import pl.socials.dto.PostInDto;
import pl.socials.dto.PostOutDto;

public interface PostService {
    PostOutDto getPost(Long id);

    List<PostOutDto> getPosts(int page, int size, String sortBy, boolean asc);

    PostOutDto createPost(PostInDto postInDto);

    void updatePost(Long id, PostInDto postInDto);

    void deletePost(Long id);
}
