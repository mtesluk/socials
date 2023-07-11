package pl.socials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.socials.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query("update Post p set p.content = :content, p.author = :author where p.id = :id")
    int updateContentAndAuthor(@Param("id") Long id, @Param("content") String content, @Param("author") String author);
}
