package pl.socials.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.socials.dto.PostOutDto;
import pl.socials.model.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(
            target = "content",
            expression =
                    "java(from.getContent() != null && from.getContent().length() >= 20 ? from.getContent().substring(0, 17).concat(\"...\") : from.getContent())")
    PostOutDto toPostDto(Post from);

    List<PostOutDto> toPostDtoList(List<Post> from);
}
