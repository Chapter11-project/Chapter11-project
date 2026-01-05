package java.org.work.backend.domain.post.dto;

import java.org.work.backend.domain.post.BoardType;

public record PostRequestDto(
        String title,
        String content,
        BoardType boardType
) {
}
