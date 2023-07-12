package pl.socials.mapper;

import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.socials.dto.PostOutDto;
import pl.socials.model.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostOutDto toPostDto(Post from);

    @Named(value = "toPostDtoShortContent")
    @Mapping(
            target = "content",
            expression =
                    "java(from.getContent() != null && from.getContent().length() >= 20 ? from.getContent().substring(0, 17).concat(\"...\") : from.getContent())")
    PostOutDto toPostDtoShortContent(Post from);

    @IterableMapping(qualifiedByName = "toPostDtoShortContent")
    List<PostOutDto> toPostDtoList(List<Post> from);
}
