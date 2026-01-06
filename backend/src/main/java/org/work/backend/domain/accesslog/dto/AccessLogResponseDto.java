package org.work.backend.domain.accesslog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.work.backend.domain.accesslog.AccessLog;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AccessLogResponseDto {
    private Long id;
    private String username;
    private String action;
    private String ip;
    private LocalDateTime timestamp;

    public static AccessLogResponseDto from(AccessLog log) {
        return new AccessLogResponseDto(
                log.getId(),
                log.getUsername(),
                log.getUri(),
                log.getIp(),
                log.getAccessedAt()
        );
    }
}
