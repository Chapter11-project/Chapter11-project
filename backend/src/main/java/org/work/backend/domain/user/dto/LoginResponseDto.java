package org.work.backend.domain.user.dto;

public record LoginResponseDto(
        String accessToken,
        String role,
        Long userId
) {}
