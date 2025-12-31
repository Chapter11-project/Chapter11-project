package org.work.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.work.backend.domain.accesslog.dto.AccessLogResponseDto;
import org.work.backend.domain.accesslog.service.AccessLogService;
import org.work.backend.domain.post.dto.PostResponseDto;
import org.work.backend.domain.post.service.PostService;
import org.work.backend.domain.user.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AccessLogService accessLogService;
    private final PostService postService;

//    전체 유저 접속 로그 조회 - 페이징 최신
    @GetMapping("/access-logs")
    public Page<AccessLogResponseDto> getAllAccessLogs(Pageable pageable) {
        return accessLogService.findAllLogs(pageable);
    }

//    전체 유저 게시글 조회 - 페이지
    @GetMapping("/mypage/posts")
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        return postService.findAllPosts(pageable);
    }

//    전체 유저 게시글 삭제
    @DeleteMapping("/mypage/posts/{postId}")
    public void deletePost(@PathVariable Long postId,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.deleteByAdmin(postId, userDetails.getUser());
    }
}
