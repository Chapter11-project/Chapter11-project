package org.example.backend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.work.backend.auth.dto.LoginRequest;
import org.work.backend.auth.dto.TokenResponse;
import org.work.backend.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
