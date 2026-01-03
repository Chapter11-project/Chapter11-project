package org.work.backend.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.work.backend.domain.admin.AdminCommentDto;
import org.work.backend.domain.admin.AdminPostDto;
import org.work.backend.domain.comment.repository.CommentRepository;
import org.work.backend.domain.post.repository.PostRepository;
import org.work.backend.domain.user.CustomUserDetails;
import org.work.backend.domain.user.Role;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 🔐 관리자 검증
    public void validateAdmin(CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getUser().getRole() != Role.ADMIN) {
            throw new RuntimeException("관리자 권한 필요");
        }
    }

    public List<AdminPostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(AdminPostDto::from)
                .toList();
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<AdminCommentDto> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(AdminCommentDto::from)
                .toList();
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
