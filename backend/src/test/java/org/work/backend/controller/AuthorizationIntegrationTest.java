package org.work.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.work.backend.common.jwt.JwtProvider;
import org.work.backend.domain.comment.Comment;
import org.work.backend.domain.comment.repository.CommentRepository;
import org.work.backend.domain.post.BoardType;
import org.work.backend.domain.post.Post;
import org.work.backend.domain.post.repository.PostRepository;
import org.work.backend.domain.user.User;
import org.work.backend.domain.user.repository.UserRepository;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthorizationIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    ObjectMapper objectMapper;

    private User author;
    private User otherUser;
    private User admin;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();

        author = userRepository.save(
                User.signup("author@test.com", passwordEncoder.encode("password"))
        );
        otherUser = userRepository.save(
                User.signup("other@test.com", passwordEncoder.encode("password"))
        );
        admin = userRepository.save(
                User.admin("admin@test.com", passwordEncoder.encode("password"))
        );

        post = postRepository.save(
                Post.create("title", "content", BoardType.GENERAL, author)
        );
        comment = commentRepository.save(
                new Comment("comment", author, post)
        );
    }

    @Test
    @DisplayName("로그인 응답에 accessToken, role, userId 포함")
    void loginResponseContainsTokenAndRoleAndUserId() throws Exception {
        String payload = objectMapper.writeValueAsString(
                Map.of(
                        "email", author.getUsername(),
                        "password", "password"
                )
        );

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.userId").value(author.getId()));
    }

    @Test
    @DisplayName("JWT 필터는 잘못된 토큰을 거부하고 올바른 토큰을 통과시킨다")
    void jwtFilterValidation() throws Exception {
        mockMvc.perform(
                        get("/api/users/me")
                                .header("Authorization", "Bearer invalid-token")
                )
                .andExpect(status().isUnauthorized());

        mockMvc.perform(
                        get("/api/users/me")
                                .header("Authorization", bearer(author))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(author.getUsername()));
    }

    @Test
    @DisplayName("게시글 삭제는 작성자나 ADMIN만 가능하다")
    void postDeleteAuthorization() throws Exception {
        mockMvc.perform(
                        delete("/api/community/{id}", post.getId())
                                .header("Authorization", bearer(otherUser))
                )
                .andExpect(status().isForbidden());

        mockMvc.perform(
                        delete("/api/community/{id}", post.getId())
                                .header("Authorization", bearer(author))
                )
                .andExpect(status().isOk());

        Post anotherPost = postRepository.save(
                Post.create("title2", "content2", BoardType.GENERAL, author)
        );

        mockMvc.perform(
                        delete("/api/community/{id}", anotherPost.getId())
                                .header("Authorization", bearer(admin))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 삭제는 작성자나 ADMIN만 가능하다")
    void commentDeleteAuthorization() throws Exception {
        mockMvc.perform(
                        delete("/api/comments/{id}", comment.getId())
                                .header("Authorization", bearer(otherUser))
                )
                .andExpect(status().isForbidden());

        mockMvc.perform(
                        delete("/api/comments/{id}", comment.getId())
                                .header("Authorization", bearer(author))
                )
                .andExpect(status().isOk());

        Comment adminComment = commentRepository.save(
                new Comment("admin comment", author, post)
        );

        mockMvc.perform(
                        delete("/api/comments/{id}", adminComment.getId())
                                .header("Authorization", bearer(admin))
                )
                .andExpect(status().isOk());
    }

    private String bearer(User user) {
        return "Bearer " + jwtProvider.generateToken(user.getUsername());
    }
}
