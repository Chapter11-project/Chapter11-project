package org.work.backend.domain.accesslog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.work.backend.domain.accesslog.AccessLog;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
