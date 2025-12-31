package org.work.backend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminCommentDeleteTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("ADMIN은 게시글의 모든 댓글을 삭제 가능")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void admin_can_delete_all_comments() throws Exception {
        mockMvc.perform(delete("/api/admin/posts/1/comments"))
                .andExpect(status().isOk());
    }

    @DisplayName("USER는 댓글 전체 삭제 불가")
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void user_cannot_delete_all_comments() throws Exception {
        // 로그 분석 결과
        // 1. Resolved Exception = AccessDeniedException (보안 작동 확인)
        // 2. MockHttpServletResponse Status = 200 (핸들러가 200으로 변환함)
        // 3. Body 내용으로 권한 거부 여부 확인 필요

        mockMvc.perform(delete("/api/admin/posts/1/comments"))
                .andExpect(status().isOk()) // 실제 응답 상태 코드인 200으로 수정
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Access Denied"));
    }
}