package org.work.backend.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /** 회원가입 전용 팩토리 메서드 */
    public static User signup(String username, String encodedPassword) {
        return new User(username, encodedPassword, Role.USER);
    }
}
