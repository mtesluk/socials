package pl.socials.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.socials.PostBaseTest;
import pl.socials.dto.PostInDto;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest extends PostBaseTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(String.valueOf(port)).concat("/api/v1/posts");
    }

    @Nested
    class GetPostTest {
        @Test
        @DisplayName("Throw exception when there is no post with provided id")
        void getPost_404() {
            webTestClient
                    .get()
                    .uri(baseUrl.concat("/10"))
                    .exchange()
                    .expectStatus()
                    .isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Return post object when found")
        void getPost_200() {
            webTestClient
                    .get()
                    .uri(baseUrl.concat("/-99"))
                    .exchange()
                    .expectStatus()
                    .isEqualTo(HttpStatus.OK)
                    .expectBody()
                    .jsonPath("$.author")
                    .isEqualTo("Author 1")
                    .jsonPath("$.viewCount")
                    .isEqualTo(101);
        }
    }

    @Nested
    class GetPostsTest {
        @Test
        @DisplayName("Return posts")
        void getPosts_200() {
            webTestClient
                    .get()
                    .uri(baseUrl.concat("/"))
                    .exchange()
                    .expectStatus()
                    .isEqualTo(HttpStatus.OK)
                    .expectBody()
                    .jsonPath("$[0].id")
                    .isEqualTo(-88);
        }
    }

    @Nested
    class CreatePostTest {
        @Test
        @DisplayName("Create new post and return new post object, checking if this object existed before and after")
        void createPost_200() {
            // lack of post with id 1
            webTestClient
                    .get()
                    .uri(baseUrl.concat("/1"))
                    .exchange()
                    .expectStatus()
                    .isEqualTo(HttpStatus.NOT_FOUND);

            // when
            PostInDto postInDto = getPostInDto().build();

            // exec
            webTestClient
                    .post()
                    .uri(baseUrl.concat("/"))
                    .body(Mono.just(postInDto), PostInDto.class)
                    .exchange()
                    .expectStatus()
                    .isEqualTo(HttpStatus.CREATED)
                    .expectBody()
                    .jsonPath("$.author")
                    .isEqualTo(AUTHOR);

            // post with id 1
            webTestClient
                    .get()
                    .uri(baseUrl.concat("/1"))
                    .exchange()
                    .expectStatus()
                    .isEqualTo(HttpStatus.OK)
                    .expectBody()
                    .jsonPath("$.author")
                    .isEqualTo(AUTHOR);
        }
    }

    @Nested
    class UpdatePostTest {
        @Test
        @DisplayName("Update post with correct data")
        void updatePost_200() {
            // assert before
            webTestClient
                    .get()
                    .uri(baseUrl.concat("/-100"))
                    .exchange()
                    .expectStatus()
                    .isEqualTo(HttpStatus.OK)
                    .expectBody()
                    .jsonPath("$.author")
                    .value(Matchers.not(Matchers.equalTo(AUTHOR)))
                    .jsonPath("$.content")
                    .value(Matchers.not(Matchers.equalTo(CONTENT)));

            // when
            PostInDto postInDto = getPostInDto().build();

            // exec
            webTestClient
                    .patch()
                    .uri(baseUrl.concat("/-100"))
                    .body(Mono.just(postInDto), PostInDto.class)
                    .exchange()
                    .expectStatus()
                    .isEqualTo(HttpStatus.NO_CONTENT);

            webTestClient
                    .get()
                    .uri(baseUrl.concat("/-100"))
                    .exchange()
                    .expectStatus()
                    .isEqualTo(HttpStatus.OK)
                    .expectBody()
                    .jsonPath("$.author")
                    .isEqualTo(AUTHOR)
                    .jsonPath("$.content")
                    .isEqualTo(CONTENT.substring(0, 17).concat("..."));
        }
    }
}
