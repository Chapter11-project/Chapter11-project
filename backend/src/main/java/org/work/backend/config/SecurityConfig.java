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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.work.backend.common.jwt.JwtAuthenticationFilter;
import org.work.backend.common.jwt.JwtProvider;
import org.work.backend.domain.user.service.CustomUserDetailsService;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ===============================
                // CSRF 비활성화
                // ===============================
                .csrf(csrf -> csrf.disable())

                // ===============================
                // 세션 미사용 (JWT)
                // ===============================
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ===============================
                // 요청별 접근 권한
                // ===============================
                .authorizeHttpRequests(auth -> auth

                        //정적 리소스 / 화면
                        .requestMatchers(
                                "/",
                                "/login.html",
                                "/signup.html",
                                "/home.html",
                                "/community.html",
                                "/qna.html",
                                "/css/**",
                                "/javascript/**",
                                "/img/**",
                                "/favicon.ico"
                        ).permitAll()

                        //인증 API
                        .requestMatchers("/auth/**").permitAll()

                        //게시글 조회
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()

                        //나머지는 인증 필요
                        .anyRequest().authenticated()
                )

                // ===============================
                // JWT 인증 필터 등록
                // ===============================
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtProvider, customUserDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                )

                // ===============================
                // 기본 로그인 / Basic 인증 비활성화
                // ===============================
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    // ===============================
    // CORS 설정
    // ===============================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://localhost:5500");
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // ===============================
    // AuthenticationManager
    // ===============================
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // ===============================
    // PasswordEncoder
    // ===============================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
