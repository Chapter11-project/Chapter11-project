package org.work.backend.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.work.backend.domain.post.BoardType;
import org.work.backend.domain.post.dto.PostRequestDto;
import org.work.backend.domain.post.dto.PostResponseDto;
import org.work.backend.domain.post.service.PostService;
import org.work.backend.domain.user.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;


    /**
     * 글 작성
     */
    @PostMapping
    public void create(
            @RequestBody PostRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postService.create(request, userDetails.getUser());
    }

    /**
     * 게시판별 조회
     */
    @GetMapping
    public List<PostResponseDto> list(@RequestParam BoardType boardType) {
        return postService.findByBoardType(boardType);
    }
}
