package org.work.backend.domain.user.dto;

public record LoginRequestDto(
        String email,
        String password
) {}
