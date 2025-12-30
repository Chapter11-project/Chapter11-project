package org.work.backend.domain.accesslog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.work.backend.domain.accesslog.AccessLog;
import org.work.backend.domain.accesslog.repository.AccessLogRepository;

@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final AccessLogRepository repository;

    public void save(String username, String ip, String uri) {
        repository.save(new AccessLog(username, ip, uri));
    }
}
