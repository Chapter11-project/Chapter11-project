package org.work.backend.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.work.backend.common.jwt.JwtProvider;
import org.work.backend.domain.user.dto.LoginRequestDto;
import org.work.backend.domain.user.dto.LoginResponseDto;
import org.work.backend.domain.user.dto.SignupRequest;
import org.work.backend.domain.user.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        String token = jwtProvider.generateToken(authentication.getName());
        return new LoginResponseDto(token);
    }
}
