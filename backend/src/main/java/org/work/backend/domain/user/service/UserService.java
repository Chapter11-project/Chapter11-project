package java.org.work.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.org.work.backend.domain.user.User;
import java.org.work.backend.domain.user.dto.SignupRequest;
import java.org.work.backend.domain.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        User user = User.signup(
                request.username(),
                passwordEncoder.encode(request.password())
        );

        userRepository.save(user);
    }
}