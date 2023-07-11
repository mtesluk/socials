package pl.socials.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostInDto {
    private String author;
    private String content;
}
