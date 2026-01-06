package org.work.backend.domain.notification.service;

import org.work.backend.domain.notification.Notification;
import org.work.backend.domain.notification.dto.NotificationResponseDto;
import org.work.backend.domain.notification.repository.NotificationRepository;
import org.work.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void notify(User recipient, User actor, String message, String link) {
        if (recipient == null) {
            return;
        }
        if (actor != null && recipient.getId().equals(actor.getId())) {
            return; // 자기 자신에게는 알림 생성 X
        }
        Notification notification = new Notification(recipient, message, link);
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> list(User user) {
        return notificationRepository.findTop20ByRecipientOrderByCreatedAtDesc(user)
                .stream()
                .map(NotificationResponseDto::from)
                .toList();
    }

    @Transactional
    public void markRead(Long notificationId, User user) {
        Notification notification = notificationRepository.findByIdAndRecipient(notificationId, user)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
        notification.markRead();
    }
}
