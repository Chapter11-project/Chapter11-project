package java.org.work.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.org.work.backend.domain.accesslog.dto.AccessLogResponseDto;
import java.org.work.backend.domain.accesslog.service.AccessLogService;
import java.org.work.backend.domain.comment.service.CommentService;
import java.org.work.backend.domain.post.dto.PostResponseDto;
import java.org.work.backend.domain.post.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AccessLogService accessLogService;
    private final PostService postService;
    private final CommentService commentService;

    //    전체 유저 접속 로그
    @GetMapping("/access-logs")
    public Page<AccessLogResponseDto> getAllAccessLogs(Pageable pageable) {
        return accessLogService.findAllLogs(pageable);
    }

    //    전체 유저 게시글 조회
    @GetMapping("/mypage/posts")
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        return postService.findAllPosts(pageable);
    }

    //    전체 유저 게시글 삭제
    @DeleteMapping("/mypage/posts/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deleteByAdmin(postId);
    }

    // 관리자 전용 댓글 전체 삭제
    @DeleteMapping("/posts/{postId}/comments")
    public void deleteAllCommentsByPost(@PathVariable Long postId) {
        commentService.deleteAllByPostId(postId);
    }

}
