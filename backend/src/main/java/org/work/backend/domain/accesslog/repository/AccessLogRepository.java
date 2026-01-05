package java.org.work.backend.domain.accesslog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.org.work.backend.domain.accesslog.AccessLog;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
