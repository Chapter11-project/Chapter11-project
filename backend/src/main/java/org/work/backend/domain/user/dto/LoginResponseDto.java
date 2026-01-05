package java.org.work.backend.domain.user.dto;

public record LoginResponseDto(
        String accessToken,
        String role,
        String username
) {
}