package org.work.backend.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.work.backend.domain.post.BoardType;
import org.work.backend.domain.post.dto.PostDetailResponseDto;
import org.work.backend.domain.post.dto.PostRequestDto;
import org.work.backend.domain.post.dto.PostResponseDto;
import org.work.backend.domain.post.service.PostService;
import org.work.backend.domain.user.CustomUserDetails;
import org.work.backend.exception.UnauthorizedException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/posts", "/api/community"})
public class PostController {

    private final PostService postService;

    /** 글 작성 */
    @PostMapping
    public void create(
            @RequestBody PostRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        postService.create(request, userDetails.getUser());
    }

    /** 게시판별 조회 */
    @GetMapping
    public List<PostResponseDto> list(
            @RequestParam(required = false) BoardType boardType
    ) {
        if (boardType != null) {
            return postService.findByBoardType(boardType);
        }
        return postService.findCommunityPosts();
    }

    @GetMapping("/{id}")
    public PostDetailResponseDto detail(@PathVariable Long id) {
        return postService.getPost(id);
    }

    /** 내가 쓴 글 */
    @GetMapping("/my")
    public List<PostResponseDto> myPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        return postService.myPosts(userDetails.getUser());
    }

    /** 삭제 */
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        postService.delete(id, userDetails.getUser());
    }
}
