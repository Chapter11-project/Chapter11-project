package org.work.backend.domain.accesslog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.work.backend.domain.accesslog.dto.AccessLogResponseDto;
import org.work.backend.domain.accesslog.service.AccessLogService;

@RestController
@RequestMapping("/api/admin/access-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAccessLogController {

    private final AccessLogService accessLogService;

    @GetMapping
    public Page<AccessLogResponseDto> getAccessLogs(
            @PageableDefault(
                    size = 20,
                    sort = "accessedAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return accessLogService.findAll(pageable);
    }
}
