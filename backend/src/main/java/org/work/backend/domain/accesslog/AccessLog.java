package org.work.backend.domain.accesslog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AccessLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String username;
    private String ipAddress;
    private String userAgent;
    private String requestUri;
    private String httpMethod;

    @CreatedDate
    private LocalDateTime accessedAt;

    public AccessLog(
            Long userId,
            String username,
            String ipAddress,
            String userAgent,
            String requestUri,
            String httpMethod
    ) {
        this.userId = userId;
        this.username = username;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.requestUri = requestUri;
        this.httpMethod = httpMethod;
    }
}
