package java.org.work.backend.domain.accesslog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String ip;
    private String uri;
    private LocalDateTime accessedAt;

    public AccessLog(String username, String ip, String uri) {
        this.username = username;
        this.ip = ip;
        this.uri = uri;
        this.accessedAt = LocalDateTime.now();
    }
}
