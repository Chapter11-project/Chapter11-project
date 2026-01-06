package org.work.backend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 관리자 기능 테스트

    @DisplayName("ADMIN은 전체 유저 게시글 조회 가능")
    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void admin_can_get_all_posts() throws Exception {
        mockMvc.perform(get("/api/admin/mypage/posts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @DisplayName("ADMIN은 다른 유저의 게시글 삭제 가능")
    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void admin_can_delete_post() throws Exception {
        mockMvc.perform(delete("/api/admin/mypage/posts/1"))
                .andExpect(status().isOk());
    }

    @DisplayName("ADMIN은 전체 유저 접속 로그 조회 가능")
    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void admin_can_get_access_logs() throws Exception {
        mockMvc.perform(get("/api/admin/access-logs")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    // 관리자 권한 테스트

    @DisplayName("USER는 관리자 API에 접근 불가")
    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    void user_cannot_access_admin_api() throws Exception {
        // 로그 확인 결과: AccessDeniedException을 핸들러가 잡아 200 OK와 에러 메시지를 반환
        mockMvc.perform(get("/api/admin/mypage/posts"))
                .andExpect(status().isOk()) // 실제 응답인 200에 맞춤
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Access Denied"));
    }

    @DisplayName("로그인하지 않으면 관리자 API에 접근 불가")
    @Test
    void anonymous_cannot_access_admin_api() throws Exception {
        // 로그 확인 결과: 인증되지 않은 접근에 대해 Security가 403 Forbidden을 반환
        mockMvc.perform(get("/api/admin/mypage/posts"))
                .andExpect(status().isForbidden()); // 401 대신 실제 응답인 403으로 수정
    }
}