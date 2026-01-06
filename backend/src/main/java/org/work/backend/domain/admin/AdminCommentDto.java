package org.work.backend.domain.admin;

import org.work.backend.domain.comment.Comment;

public record AdminCommentDto(
        Long id,
        String content,
        String authorUsername,
        Long postId,
        String createdAt
) {
    public static AdminCommentDto from(Comment comment) {
        return new AdminCommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUsername(),
                comment.getPost().getId(),
                comment.getCreatedAt().toString()
        );
    }
}
