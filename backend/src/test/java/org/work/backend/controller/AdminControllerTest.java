package org.work.backend.controller;

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
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 전체 유저 게시글 조회 (페이징)
    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void testGetAllPosts() throws Exception {
        mockMvc.perform(get("/api/admin/mypage/posts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    // 전체 글 삭제 (ADMIN 권한)
    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void testDeletePost() throws Exception {
        mockMvc.perform(delete("/api/admin/mypage/posts/1"))
                .andExpect(status().isOk());
    }

    // 전체 유저 접속 로그 조회 (페이징)
    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void testGetAllAccessLogs() throws Exception {
        mockMvc.perform(get("/api/admin/access-logs")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
