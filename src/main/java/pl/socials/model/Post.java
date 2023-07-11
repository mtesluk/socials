package pl.socials.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "date", nullable = false)
    private OffsetDateTime createdDate;

    @Column(name = "viewCount", nullable = false)
    private Long viewCount;

    @PrePersist
    protected void onCreate() {
        this.createdDate = OffsetDateTime.now();
        this.viewCount = 0L;
    }
}
