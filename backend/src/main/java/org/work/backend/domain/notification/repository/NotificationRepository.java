package org.work.backend.domain.notification.repository;

import org.work.backend.domain.notification.Notification;
import org.work.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findTop20ByRecipientOrderByCreatedAtDesc(User recipient);
    Optional<Notification> findByIdAndRecipient(Long id, User recipient);
}