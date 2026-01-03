package org.work.backend.domain.accesslog;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.work.backend.common.jwt.JwtProvider;
import org.work.backend.domain.accesslog.service.AccessLogService;

@Component
@RequiredArgsConstructor
public class AccessLogInterceptor implements HandlerInterceptor {

    private final AccessLogService accessLogService;
    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {

        Long userId = null;
        String username = "ANONYMOUS";

        String token = jwtProvider.resolveToken(request);
        if (token != null && jwtProvider.validateToken(token)) {
            username = jwtProvider.getUsername(token);
            // 필요시 userId도 JWT claim에 포함하고 가져올 수 있음
        }

        accessLogService.save(
                userId,
                username,
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                request.getRequestURI(),
                request.getMethod()
        );

        return true;
    }
}
