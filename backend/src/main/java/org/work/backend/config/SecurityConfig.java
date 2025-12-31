package org.work.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
// ↑ @PreAuthorize, @PostAuthorize 같은 메서드 단위 권한 체크를 활성화
//   → 컨트롤러/서비스 레벨에서 세밀한 권한 제어 가능
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ===============================
            // CSRF 비활성화
            // ===============================
            .csrf(csrf -> csrf.disable())
                /* JWT 기반 인증에서는 서버 세션을 사용하지 않으므로
                * CSRF 토큰이 의미 없음
                * (REST API + Stateless 구조에서는 관례적으로 disable)*/

                // ===============================
                // 요청별 접근 권한 설정
                // ===============================
                .authorizeHttpRequests(auth -> auth

                // -------------------------------
                //  정적 리소스 & HTML 화면
                // -------------------------------
                .requestMatchers(
                        "/",
                        "/**/*.html",      // write.html, login.html, signup.html 등
                        "/css/**",
                        "/javascript/**",
                        "/images/**",
                        "/img/**"
                ).permitAll()
                        /*프론트엔드를 Spring에서 정적으로 서빙하는 구조
                        로그인 여부와 상관없이 화면 접근은 허용
                        여기 막아버리면 브라우저에서 HTML 자체가 안 열림*/

                // -------------------------------
                // 인증 관련 API
                // -------------------------------
                .requestMatchers("/api/auth/**").permitAll()
                       /* 로그인 / 회원가입 / 토큰 재발급
                        → 인증 이전에 접근 가능해야 함*/


                // -------------------------------
                // 게시글 조회 (읽기 전용)
                // -------------------------------
                .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                        /*비로그인 사용자도 게시글 조회 가능
                        POST / PUT / DELETE 는 아래에서 인증 필요*/

                // -------------------------------
                // 그 외 모든 요청
                // -------------------------------
                .anyRequest().authenticated()
                        /*위에서 허용하지 않은 모든 요청은
                        JWT 인증이 반드시 필요*/
                )

                // ===============================
                // 기본 인증 방식 비활성화
                // ===============================
                .formLogin(form -> form.disable())
                /*
                 * Spring Security 기본 로그인 페이지 사용 안 함
                 * → 프론트엔드에서 직접 로그인 UI 처리
                 */

                .httpBasic(basic -> basic.disable())
                /*
                 * Authorization: Basic xxx 방식 비활성화
                 * → JWT 토큰 기반 인증만 사용
                 */

                // ===============================
                // 세션 관리 정책
                // ===============================
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        /*
         * 서버 세션 생성 x
         * 요청마다 JWT로 인증 정보 판단
         * (완전한 RESTful API 구조)
         */

        return http.build();
    }

    // ===============================
    // CORS 설정
    // ===============================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // 프론트엔드 개발 서버 허용
        config.addAllowedOrigin("http://localhost:3000"); // React
        config.addAllowedOrigin("http://localhost:5500"); // Live Server
        config.addAllowedOrigin("http://localhost:8080"); // 동일 서버

        config.addAllowedMethod("*");   // GET, POST, PUT, DELETE 등
        config.addAllowedHeader("*");   // Authorization, Content-Type 등
        config.setAllowCredentials(true);
        /*
         * credentials = true
         * → Authorization 헤더(JWT) 허용
         *
         * 이 경우 allowedOrigin에 "*" 사용 불가
         */

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 모든 API 경로에 CORS 설정 적용

        return source;
    }

    // ===============================
    // AuthenticationManager
    // ===============================
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        /*
         * 로그인 시
         * UsernamePasswordAuthenticationToken을
         * 실제 인증 로직(UserDetailsService)으로 넘겨주는 핵심 객체
         */
        return authenticationConfiguration.getAuthenticationManager();
    }

    // ===============================
    // PasswordEncoder
    // ===============================
    @Bean
    public PasswordEncoder passwordEncoder() {
        /*
         * BCrypt
         * - 단방향 해시
         * - Salt 자동 적용
         * - 실무 표준
         */
        return new BCryptPasswordEncoder();
    }
}
