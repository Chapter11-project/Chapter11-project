package org.work.backend.controller;

import org.work.backend.domain.accesslog.AccessLog;
import org.work.backend.domain.accesslog.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AccessLogRepository accessLogRepository;

    @GetMapping("/access-logs")
    public List<AccessLog> logs() {
        return accessLogRepository.findAll();
    }
}