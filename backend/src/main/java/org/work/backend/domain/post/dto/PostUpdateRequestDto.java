package org.work.backend.domain.post.dto;

import org.work.backend.domain.post.BoardType;

public record PostUpdateRequestDto(
        String title,
        String content,
        BoardType boardType
) {
}
