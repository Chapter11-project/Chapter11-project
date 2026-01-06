package org.work.backend.domain.admin;

import org.work.backend.domain.post.Post;

public record AdminPostDto(
        Long id,
        String title,
        String authorUsername,
        String boardType,
        String createdAt
) {
    public static AdminPostDto from(Post post) {
        return new AdminPostDto(
                post.getId(),
                post.getTitle(),
                post.getUser().getUsername(),
                post.getBoardType().name(),
                post.getCreatedAt().toString()
        );
    }
}
