package org.work.backend.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.work.backend.domain.comment.Comment;
import org.work.backend.domain.comment.dto.CommentRequestDto;
import org.work.backend.domain.comment.dto.CommentResponseDto;
import org.work.backend.domain.comment.repository.CommentRepository;
import org.work.backend.domain.post.Post;
import org.work.backend.domain.post.repository.PostRepository;
import org.work.backend.domain.user.Role;
import org.work.backend.domain.user.User;
import org.work.backend.exception.AccessDeniedException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto create(Long postId, CommentRequestDto request, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        Comment comment = new Comment(request.getContent(), user, post);
        Comment saved = commentRepository.save(comment);
        return CommentResponseDto.from(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findByPost(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(CommentResponseDto::from)
                .toList();
    }

    @Transactional
    public void delete(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (user.getRole() != Role.ADMIN &&
                !comment.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("삭제 권한 없음");
        }

        commentRepository.delete(comment);
    }

    // 관리자 삭제 메서드 추가
    @Transactional
    public void deleteAllByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
    }

}
