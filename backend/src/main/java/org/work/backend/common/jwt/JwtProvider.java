package jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.org.work.backend.domain.user.Role;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {

    private final Key key =
            Keys.hmacShaKeyFor("very-secret-key-change-this-very-secret".getBytes());

    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    /**
     * JWT 생성
     */
    public String generateToken(String username, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT에서 username 추출
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    /**
     * JWT 검증
     */
    public boolean validateToken(String token) {
        try {
            getUsername(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
