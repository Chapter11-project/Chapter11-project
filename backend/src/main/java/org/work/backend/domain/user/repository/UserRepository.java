package java.org.work.backend.domain.user.repository;

import java.org.work.backend.domain.user.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.org.work.backend.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}