package org.work.backend.domain.notification.controller;

import org.work.backend.domain.notification.dto.NotificationResponseDto;
import org.work.backend.domain.notification.service.NotificationService;
import org.work.backend.domain.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.work.backend.common.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<List<NotificationResponseDto>> list(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(notificationService.list(userDetails.getUser()));
    }

    @PostMapping("/read/{id}")
    public ApiResponse<Void> read(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        notificationService.markRead(id, userDetails.getUser());
        return ApiResponse.ok();
    }
}