package org.work.backend.domain.comment.dto;

import lombok.Getter;
import org.work.backend.domain.comment.Comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        String content,
        Long authorId,
        String authorUsername,
        LocalDateTime createdAt
) {
    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getCreatedAt()
        );
    }
}
