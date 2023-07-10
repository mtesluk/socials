package pl.socials.dto;

import java.time.OffsetDateTime;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostOutDto {
    private Long id;
    private String author;
    private String content;
    private OffsetDateTime date;
    private Long viewCount;
}
