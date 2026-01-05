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
import org.work.backend.exception.CommentNotFoundException;
import org.work.backend.exception.PostNotFoundException;
import org.work.backend.exception.UnauthorizedException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void delete(Long commentId, User user) {
        if (user == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));

        if (user.getRole() == Role.ADMIN ||
                comment.getAuthor().getId().equals(user.getId())) {
            commentRepository.delete(comment);
            return;
        }

        throw new AccessDeniedException("댓글 삭제 권한이 없습니다.");
    }

    @Transactional
    public CommentResponseDto create(Long postId, CommentRequestDto requestDto, User user) {
        if (user == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment(requestDto.getContent(), user, post);
        return CommentResponseDto.from(commentRepository.save(comment));
    }

    public List<CommentResponseDto> findByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        return commentRepository.findByPost(post)
                .stream()
                .map(CommentResponseDto::from)
                .toList();
    }
}
