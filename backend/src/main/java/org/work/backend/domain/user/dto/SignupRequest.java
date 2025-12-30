package org.work.backend.domain.user.dto;

public record SignupRequest(
        String username,
        String password
) {}