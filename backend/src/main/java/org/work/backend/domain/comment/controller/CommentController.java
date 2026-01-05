package org.work.backend.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.work.backend.domain.comment.dto.CommentRequestDto;
import org.work.backend.domain.comment.dto.CommentResponseDto;
import org.work.backend.domain.comment.service.CommentService;
import org.work.backend.domain.user.CustomUserDetails;
import org.work.backend.exception.UnauthorizedException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/community/{postId}/comments")
    public List<CommentResponseDto> list(@PathVariable Long postId) {
        return commentService.findByPost(postId);
    }

    @PostMapping("/community/{postId}/comments")
    public CommentResponseDto create(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        return commentService.create(postId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/comments/{commentId}")
    public void delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        commentService.delete(commentId, userDetails != null ? userDetails.getUser() : null);
    }
}
