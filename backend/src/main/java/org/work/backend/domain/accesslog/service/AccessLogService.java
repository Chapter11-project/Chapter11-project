package org.work.backend.domain.accesslog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.work.backend.domain.accesslog.AccessLog;
import org.work.backend.domain.accesslog.dto.AccessLogResponseDto;
import org.work.backend.domain.accesslog.repository.AccessLogRepository;

@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final AccessLogRepository repository;

    public void save(String username, String ip, String uri) {
        repository.save(new AccessLog(null, username, ip, null, uri, null));
    }

    public void save(
            Long userId,
            String username,
            String ipAddress,
            String userAgent,
            String requestUri,
            String httpMethod
    ) {
        repository.save(new AccessLog(
                userId,
                username,
                ipAddress,
                userAgent,
                requestUri,
                httpMethod
        ));
    }

    public Page<AccessLogResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(AccessLogResponseDto::from);
    }
}
