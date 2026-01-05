package java.org.work.backend.domain.comment.dto;

import java.org.work.backend.domain.comment.Comment;

public record CommentResponseDto(
        Long id,
        String content,
        String authorUsername
) {

    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getUsername()
        );
    }
}