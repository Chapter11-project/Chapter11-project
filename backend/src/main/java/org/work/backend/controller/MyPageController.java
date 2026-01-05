package java.org.work.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.org.work.backend.domain.post.dto.PostResponseDto;
import java.org.work.backend.domain.post.service.PostService;
import java.org.work.backend.domain.user.CustomUserDetails;
import java.org.work.backend.domain.user.User;
import java.org.work.backend.domain.user.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final PostService postService;
    private final UserService userService;

    //    내가 작성한 게시글 조회 - 페이징
    @GetMapping("/posts")
    public Page<PostResponseDto> myPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable
    ) {
        return postService.myPosts(userDetails.getUser(), pageable);
    }

    //     내가 작성한 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public void deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postService.delete(postId, userDetails.getUser());
    }

    //    내가 작성한 게시글 조회 - 전체
    @GetMapping("/posts/all")
    public List<PostResponseDto> myPostsAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return postService.myPosts(userDetails.getUser());
    }
}
