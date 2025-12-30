package org.work.backend.domain.post.service;

import org.work.backend.domain.post.Post;
import org.work.backend.domain.post.repository.PostRepository;
import org.work.backend.domain.user.Role;
import org.work.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post create(Post post) {
        return postRepository.save(post);
    }

    public void delete(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        if (currentUser.getRole() == Role.ADMIN ||
                post.getAuthor().getId().equals(currentUser.getId())) {
            postRepository.delete(post);
            return;
        }

        throw new RuntimeException("삭제 권한 없음");
    }
}