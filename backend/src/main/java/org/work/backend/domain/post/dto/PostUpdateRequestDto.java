package org.work.backend.domain.post.dto;

public record PostUpdateRequestDto(
        String title,
        String content
) {
}