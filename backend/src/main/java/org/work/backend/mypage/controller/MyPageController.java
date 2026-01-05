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
import org.work.backend.exception.UnauthorizedException;

import org.work.backend.domain.comment.Comment;
import org.work.backend.domain.post.Post;
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
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        User user = userDetails.getUser();

        boolean isAdmin = user.getRole() == Role.ADMIN;

        List<Post> posts = isAdmin ? postRepository.findAll() : postRepository.findByAuthor(user);
        List<Comment> comments = isAdmin ? commentRepository.findAll() : commentRepository.findByAuthor(user);

        return MyPageResponse.builder()
                .posts(posts)
                .comments(comments)
                .accessLogs(isAdmin ? accessLogRepository.findAll() : List.of())
                .build();
    }
}
