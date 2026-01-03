package org.work.backend.mypage.controller;

import org.work.backend.domain.accesslog.repository.AccessLogRepository;
import org.work.backend.domain.comment.repository.CommentRepository;
import org.work.backend.domain.post.repository.PostRepository;
import org.work.backend.domain.user.CustomUserDetails;
import org.work.backend.domain.user.Role;
import org.work.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.work.backend.mypage.dto.MyPageResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AccessLogRepository accessLogRepository;

    @GetMapping
    public MyPageResponse myPage(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new RuntimeException("로그인 필요");
        }

        User user = userDetails.getUser();

        return MyPageResponse.builder()
                .posts(postRepository.findByUser(user))   // ✅ 여기 수정
                .comments(commentRepository.findByUser(user))
                .accessLogs(
                        user.getRole() == Role.ADMIN
                                ? accessLogRepository.findAll()
                                : List.of()
                )
                .build();
    }
}
