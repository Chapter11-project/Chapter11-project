package org.work.backend.domain.notification;

import org.work.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User recipient;

    private String message;

    private String link;

    private boolean isRead = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Notification(User recipient, String message, String link) {
        this.recipient = recipient;
        this.message = message;
        this.link = link;
    }

    public void markRead() {
        this.isRead = true;
    }
}
