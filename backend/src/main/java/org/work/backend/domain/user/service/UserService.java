package org.work.backend.domain.user.service;

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

    public void signup(SignupRequest request) {

        String encodedPassword =
                passwordEncoder.encode(request.password());

        User user = User.signup(
                request.username(),
                encodedPassword
        );

        userRepository.save(user);
    }
}
