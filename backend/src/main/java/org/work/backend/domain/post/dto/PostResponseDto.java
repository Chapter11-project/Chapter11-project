package org.work.backend.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.work.backend.domain.post.Post;
import org.work.backend.domain.post.BoardType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private BoardType boardType;
    private LocalDateTime createdAt;

    public static PostResponseDto from(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getUsername(),
                post.getBoardType(),
                post.getCreatedAt()
        );
    }
}