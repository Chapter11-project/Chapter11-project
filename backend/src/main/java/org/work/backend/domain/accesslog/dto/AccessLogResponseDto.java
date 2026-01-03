package org.work.backend.domain.accesslog.dto;


import org.work.backend.domain.accesslog.AccessLog;

import java.time.LocalDateTime;

public record AccessLogResponseDto(
        String username,
        String ipAddress,
        String requestUri,
        String httpMethod,
        LocalDateTime accessedAt
) {
    public static AccessLogResponseDto from(AccessLog log) {
        return new AccessLogResponseDto(
                log.getUsername(),
                log.getIpAddress(),
                log.getRequestUri(),
                log.getHttpMethod(),
                log.getAccessedAt()
        );
    }
}
