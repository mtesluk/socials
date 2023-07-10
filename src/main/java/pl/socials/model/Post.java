package pl.socials.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "viewCount")
    private Long viewCount;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
}
