package org.work.backend.infra.logging;

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
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String username = "ANONYMOUS";

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtProvider.validateToken(token)) {
                username = jwtProvider.getUsername(token);
            }
        }

        accessLogService.save(
                null,
                username,
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                request.getRequestURI(),
                request.getMethod()
        );
        return true;
    }
}
