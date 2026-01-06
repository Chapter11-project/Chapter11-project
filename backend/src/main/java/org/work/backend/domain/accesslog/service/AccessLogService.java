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
        repository.save(new AccessLog(username, ip, uri));
    }

    // 전체 로그 조회 (페이징, 최신순)
    public Page<AccessLogResponseDto> findAllLogs(Pageable pageable) {
        return repository.findAll(pageable)
                .map(AccessLogResponseDto::from);
    }
}
