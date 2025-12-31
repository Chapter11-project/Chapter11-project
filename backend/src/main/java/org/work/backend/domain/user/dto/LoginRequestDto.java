package org.work.backend.domain.user.dto;

public record LoginRequestDto(
        String username,
        String password
) {}