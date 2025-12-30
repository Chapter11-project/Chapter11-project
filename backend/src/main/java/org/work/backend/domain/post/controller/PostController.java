package org.work.backend.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.work.backend.domain.post.Post;
import org.work.backend.domain.post.repository.PostRepository;
import org.work.backend.domain.user.CustomUserDetails;
import org.work.backend.domain.user.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostRepository postRepository;

    @GetMapping("/posts")
    public List<Post> myPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        return postRepository.findByAuthor(user);
    }
}
