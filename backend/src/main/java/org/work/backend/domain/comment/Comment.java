package org.work.backend.domain.comment;


import org.work.backend.domain.post.Post;
import org.work.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public Comment(String content, User author, Post post) {
        this.content = content;
        this.author = author;
        this.post = post;
    }
}