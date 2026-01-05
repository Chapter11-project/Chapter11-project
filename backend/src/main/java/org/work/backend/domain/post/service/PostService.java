package org.work.backend.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.work.backend.domain.post.BoardType;
import org.work.backend.domain.post.Post;
import org.work.backend.domain.post.dto.PostRequestDto;
import org.work.backend.domain.post.dto.PostResponseDto;
import org.work.backend.domain.post.dto.PostUpdateRequestDto;
import org.work.backend.domain.post.repository.PostRepository;
import org.work.backend.domain.user.Role;
import org.work.backend.domain.user.User;
import org.work.backend.exception.AccessDeniedException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * 글 작성
     */
    @Transactional
    public void create(PostRequestDto request, User user) {
        Post post = Post.create(
                request.title(),
                request.content(),
                request.boardType(),
                user
        );
        postRepository.save(post);
    }

    /**
     * 게시판별 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponseDto> findByBoardType(BoardType boardType) {
        return postRepository.findByBoardType(boardType)
                .stream()
                .map(PostResponseDto::from)
                .toList();
    }

    /**
     * 내가 쓴 글 조회 - 페이징
     */
    @Transactional(readOnly = true)
    public Page<PostResponseDto> myPosts(User user, Pageable pageable) {
        return postRepository.findByAuthor(user, pageable)
                .map(PostResponseDto::from);
    }

    /**
     * 내가 쓴 글 조회 - 전체
     */
    @Transactional(readOnly = true)
    public List<PostResponseDto> myPosts(User user) {
        return postRepository.findByAuthor(user)
                .stream()
                .map(PostResponseDto::from)
                .toList();
    }

    /**
     * 게시글 상세
     */
    @Transactional(readOnly = true)
    public PostResponseDto findById(Long postId) {
        return postRepository.findById(postId)
                .map(PostResponseDto::from)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
    }

    /**
     * 게시글 수정 - 작성자만 허용
     */
    @Transactional
    public void update(Long postId, PostUpdateRequestDto request, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("수정 권한 없음");
        }

        post.update(request.title(), request.content());
    }

    /**
     * 관리자 전체 게시글 조회
     */
    @Transactional(readOnly = true)
    public Page<PostResponseDto> findAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostResponseDto::from);
    }

    /**
     * 관리자 삭제 (권한 체크  → Security에서 이미 보장됨)
     */
    @Transactional
    public void deleteByAdmin(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
        postRepository.delete(post);
    }

    /**
     * 일반 유저 삭제
     */
    @Transactional
    public void delete(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        if (currentUser.getRole() != Role.ADMIN &&
                !post.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("삭제 권한 없음");
        }

        postRepository.delete(post);
    }
}
