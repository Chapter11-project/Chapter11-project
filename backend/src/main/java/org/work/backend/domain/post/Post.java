package org.work.backend.domain.post;

import org.work.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    public Post(String title, String content, BoardType boardType, User author) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}