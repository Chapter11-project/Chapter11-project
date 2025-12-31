package org.work.backend.infra.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.work.backend.common.util.SecurityUtil;
import org.work.backend.domain.accesslog.service.AccessLogService;

@Component
@RequiredArgsConstructor
public class AccessLogInterceptor implements HandlerInterceptor {

    private final AccessLogService accessLogService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        accessLogService.save(
                SecurityUtil.getCurrentUsername(),
                request.getRemoteAddr(),
                request.getRequestURI()
        );
        return true;
    }
}
