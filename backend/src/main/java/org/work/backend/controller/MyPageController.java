package org.work.backend.controller;

import org.work.backend.domain.post.Post;
import org.work.backend.domain.post.repository.PostRepository;
import org.work.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final PostRepository postRepository;

    @GetMapping("/posts")
    public List<Post> myPosts(@AuthenticationPrincipal User user) {
        return postRepository.findByAuthor(user);
    }
}