package org.work.backend.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.work.backend.domain.user.CustomUserDetails;
import org.work.backend.domain.user.User;
import org.work.backend.domain.user.dto.UserResponseDto;
import org.work.backend.exception.UnauthorizedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public UserResponseDto me(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new UnauthorizedException("로그인 필요");
        }

        User user = userDetails.getUser();

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}
