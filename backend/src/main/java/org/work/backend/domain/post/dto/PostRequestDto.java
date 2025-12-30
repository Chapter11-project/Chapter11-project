package org.work.backend.domain.post.dto;

import org.work.backend.domain.post.BoardType;

public record PostRequestDto(
        String title,
        String content,
        BoardType boardType
) {
}
