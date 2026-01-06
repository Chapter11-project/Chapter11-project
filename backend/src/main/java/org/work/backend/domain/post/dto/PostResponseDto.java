package org.work.backend.domain.post.dto;

import org.work.backend.domain.post.Post;

import java.time.LocalDateTime;

public record PostResponseDto(
        Long id,
        String title,
        String content,
        String writer,
        Long userId,
        LocalDateTime createdAt
) {
    public static PostResponseDto from(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUsername(),
                post.getUser().getId(),
                post.getCreatedAt()
        );
    }
}
