package org.work.backend.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
}