package pl.socials.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PostInDto {
    private String author;
    private String content;
}
