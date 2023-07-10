package pl.socials.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import pl.socials.dto.PostOutDto;
import pl.socials.model.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostOutDto toPostDto(Post from);

    List<PostOutDto> toPostDtoList(List<Post> from);
}
