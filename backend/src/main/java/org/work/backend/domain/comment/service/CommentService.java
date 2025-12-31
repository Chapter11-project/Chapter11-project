package org.work.backend.domain.comment.service;

import org.work.backend.domain.comment.Comment;
import org.work.backend.domain.comment.repository.CommentRepository;
import org.work.backend.domain.user.Role;
import org.work.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void delete(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (user.getRole() == Role.ADMIN ||
                comment.getAuthor().getId().equals(user.getId())) {
            commentRepository.delete(comment);
            return;
        }

        throw new RuntimeException("삭제 권한 없음");
    }

    // 관리자 삭제 메서드 추가
    public void deleteAllByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
    }

}