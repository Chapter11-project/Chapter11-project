package org.work.backend.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.work.backend.domain.comment.dto.CommentRequestDto;
import org.work.backend.domain.comment.dto.CommentResponseDto;
import org.work.backend.domain.comment.service.CommentService;
import org.work.backend.domain.user.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

        private final CommentService commentService;

        @PostMapping("/post/{postId}")
        public CommentResponseDto create(
                @PathVariable Long postId,
                @RequestBody CommentRequestDto request,
                @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
            return commentService.create(postId, request, userDetails.getUser());
        }

        @GetMapping("/post/{postId}")
        public List<CommentResponseDto> comments(@PathVariable Long postId) {
            return commentService.findByPost(postId);
        }

        @DeleteMapping("/{commentId}")

            public void delete(
                    @PathVariable Long commentId,
                    @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
                commentService.delete(commentId, userDetails.getUser());
            }
        }