package pl.socials;

import java.time.OffsetDateTime;
import pl.socials.dto.PostInDto;
import pl.socials.model.Post;

public class PostBaseTest {
    protected static final Long POST_ID = 1L;
    protected static final String AUTHOR = "AUTHOR 1000";
    protected static final String CONTENT = "TEXT TEXT TEXT TEXT TEXT TEXT";
    protected static final Long VIEW_COUNT = 12L;
    protected static final OffsetDateTime DATE = OffsetDateTime.now();

    protected Post.PostBuilder getPostEntity() {
        return Post.builder()
                .author(AUTHOR)
                .content(CONTENT)
                .createdDate(DATE)
                .viewCount(VIEW_COUNT)
                .id(POST_ID);
    }

    protected PostInDto.PostInDtoBuilder getPostInDto() {
        return PostInDto.builder().author(AUTHOR).content(CONTENT);
    }
}
