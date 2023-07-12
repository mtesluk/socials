package pl.socials.dto;

import java.time.OffsetDateTime;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PostOutDto {
    private Long id;
    private String author;
    private String content;
    private OffsetDateTime createdDate;
    private Long viewCount;
}
