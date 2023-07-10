package pl.socials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.socials.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {}
