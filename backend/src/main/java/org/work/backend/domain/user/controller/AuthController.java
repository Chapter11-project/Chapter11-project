package java.org.work.backend.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jwt.JwtProvider;
import java.org.work.backend.domain.user.CustomUserDetails;
import java.org.work.backend.domain.user.dto.LoginRequestDto;
import java.org.work.backend.domain.user.dto.LoginResponseDto;

@RestController
@RequestMapping("/api/auth")
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


        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtProvider.generateToken(
                principal.getUsername(),
                principal.getUser().getRole()
        );
        return new LoginResponseDto(token, principal.getUser().getRole().name(), principal.getUsername());
    }
}