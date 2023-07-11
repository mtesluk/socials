package pl.socials.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.*;
import pl.socials.PostBaseTest;
import pl.socials.dto.PostInDto;
import pl.socials.dto.PostOutDto;
import pl.socials.exception.InvalidRequestException;
import pl.socials.exception.NotFoundException;
import pl.socials.mapper.PostMapper;
import pl.socials.mapper.PostMapperImpl;
import pl.socials.model.Post;
import pl.socials.repository.PostRepository;

public class PostServiceTest extends PostBaseTest {

    private final PostRepository postRepository = mock(PostRepository.class);

    private final PostMapper postMapper = new PostMapperImpl();

    private PostService postService;

    @BeforeEach
    void initUseCase() {
        postService = new PostServiceImpl(postRepository, postMapper);
    }

    @Nested
    class GetPostTest {

        @Test
        @DisplayName("Throw exception when there is no post with provided id")
        void getPost_exception() {
            // when
            Long id = 1L;
            when(postRepository.findById(id)).thenReturn(Optional.empty());

            // exec and assert
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> postService.getPost(id));
            assertEquals(notFoundException.getMessage(), PostServiceImpl.EXC_MSG_POST_NOT_FOUND);
        }

        @ParameterizedTest
        @ValueSource(strings = {AUTHOR})
        @NullSource
        @DisplayName("Return post object when found")
        void getPost_return(String author) {
            // when
            Post postEntity = getPostEntity().build();
            postEntity.setAuthor(author);
            when(postRepository.findById(POST_ID)).thenReturn(Optional.of(postEntity));

            // exec
            PostOutDto post = postService.getPost(POST_ID);

            // assert
            assertEquals(post.getAuthor(), author);
            assertEquals(post.getContent(), CONTENT);
            assertEquals(post.getId(), POST_ID);
            assertEquals(post.getViewCount(), VIEW_COUNT);
            assertEquals(post.getCreatedDate(), DATE);
        }
    }

    @Nested
    class GetPostsTest {

        @Test
        @DisplayName("Return posts object")
        void getPosts_return() {
            // when
            Post postEntity = getPostEntity().build();
            Post postEntity2 = getPostEntity()
                    .author("AUTHOR 2")
                    .viewCount(VIEW_COUNT + 10)
                    .build();
            List<Post> providedPosts = List.of(postEntity2, postEntity);
            Page<Post> pagedTasks = new PageImpl<>(providedPosts);
            when(postRepository.findAll(any(Pageable.class))).thenReturn(pagedTasks);

            // exec
            List<PostOutDto> expectedPosts = postService.getPosts(0, 10, "viewCount", false);

            // assert
            assertEquals(expectedPosts.size(), 2);
            assertEquals(expectedPosts.get(1).getAuthor(), AUTHOR);
        }
    }

    @Nested
    class CreatePostTest {

        @Test
        @DisplayName("Create new post and return new post object")
        void createPost_returnPost() {
            // when
            PostInDto postInDto = getPostInDto().build();
            Post postEntity = getPostEntity()
                    .content(postInDto.getContent())
                    .author(postInDto.getAuthor())
                    .build();
            when(postRepository.save(postEntity)).thenReturn(postEntity);

            // exec
            PostOutDto createdPost = postService.createPost(postInDto);

            // assert
            assertEquals(createdPost.getAuthor(), postInDto.getAuthor());
            assertEquals(createdPost.getContent(), postInDto.getContent());
            verify(postRepository, times(1)).save(any());
        }
    }

    @Nested
    class DeletePostTest {

        @Test
        @DisplayName("Delete post")
        void deletePost_returnPost() {
            // exec
            postService.deletePost(POST_ID);

            // assert
            verify(postRepository, times(1)).deleteById(any());
        }
    }

    @Nested
    class UpdatePostTest {

        @Test
        @DisplayName("Throw error when theres no content and author in input data")
        void updatePost_no_content_author() {
            // when
            PostInDto postInDto = getPostInDto().content(null).author(null).build();

            // exec and assert
            InvalidRequestException invalidRequestException =
                    assertThrows(InvalidRequestException.class, () -> postService.updatePost(POST_ID, postInDto));
            assertEquals(invalidRequestException.getMessage(), PostServiceImpl.EXC_MSG_POST_UPD_WRONG_DATA);
        }

        @Test
        @DisplayName("Update post")
        void updatePost() {
            // when
            PostInDto postInDto = getPostInDto().build();

            // exec
            postService.updatePost(POST_ID, postInDto);

            // assert
            verify(postRepository, times(1)).updateContentAndAuthor(any(), any(), any());
        }
    }
}
