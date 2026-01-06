package org.work.backend.domain.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.work.backend.domain.comment.Comment;
import org.work.backend.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("post")
    private List<Comment> comments = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    private Post(String title, String content, BoardType boardType, User author) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.author = author;
    }


    public static Post create(
            String title,
            String content,
            BoardType boardType,
            User author
    ) {
        return new Post(title, content, boardType, author);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public User getUser() {
        return author;
    }
}
