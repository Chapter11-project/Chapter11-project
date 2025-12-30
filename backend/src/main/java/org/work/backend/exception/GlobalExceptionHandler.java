package org.work.backend.exception;

import org.springframework.web.bind.annotation.*;
import org.work.backend.common.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<Void> accessDenied(AccessDeniedException e) {
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Void> runtime(RuntimeException e) {
        return ApiResponse.error(e.getMessage());
    }
}
