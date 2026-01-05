package org.work.backend.domain.user.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.work.backend.domain.user.User;
import org.work.backend.domain.user.dto.SignupRequest;
import org.work.backend.domain.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signup(SignupRequest request) {

        if (userRepository.existsByUsername(request.email())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        User user = User.signup(
                request.email(),
                passwordEncoder.encode(request.password())
        );

        return userRepository.save(user);
    }

    public User promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.promoteToAdmin();
        return user;
    }

    @PostConstruct
    public void createDefaultAdmin() {
        String adminEmail = "admin@local";
        if (!userRepository.existsByUsername(adminEmail)) {
            User admin = User.admin(
                    adminEmail,
                    passwordEncoder.encode("admin1234")
            );
            userRepository.save(admin);
        }
    }
}
