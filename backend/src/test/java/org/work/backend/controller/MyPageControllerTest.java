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
public class MyPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 페이징 포함 내가 쓴 글 조회
    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    void testMyPostsPaging() throws Exception {
        mockMvc.perform(get("/api/mypage/posts")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    // 본인 글 삭제
    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    void testDeleteOwnPost() throws Exception {
        mockMvc.perform(delete("/api/mypage/posts/1"))
                .andExpect(status().isOk());
    }

    // 전체 글 조회
    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    void testMyPostsAll() throws Exception {
        mockMvc.perform(get("/api/mypage/posts/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
