package org.work.backend.domain.post.dto;

import org.work.backend.domain.comment.dto.CommentResponseDto;
import org.work.backend.domain.post.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponseDto(
        Long id,
        String title,
        String content,
        String writer,
        Long userId,
        LocalDateTime createdAt,
        List<CommentResponseDto> comments
) {
    public static PostDetailResponseDto from(Post post) {
        return new PostDetailResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUsername(),
                post.getUser().getId(),
                post.getCreatedAt(),
                post.getComments()
                        .stream()
                        .map(CommentResponseDto::from)
                        .toList()
        );
    }
}
