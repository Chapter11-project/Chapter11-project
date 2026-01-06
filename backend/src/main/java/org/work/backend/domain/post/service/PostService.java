package org.work.backend.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.work.backend.domain.post.dto.PostDetailResponseDto;
import org.work.backend.domain.post.BoardType;
import org.work.backend.domain.post.Post;
import org.work.backend.domain.post.dto.PostRequestDto;
import org.work.backend.domain.post.dto.PostResponseDto;
import org.work.backend.domain.post.repository.PostRepository;
import org.work.backend.domain.user.Role;
import org.work.backend.domain.user.User;
import org.work.backend.exception.AccessDeniedException;
import org.work.backend.exception.PostNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    /** 글 작성 */
    @Transactional
    public void create(PostRequestDto request, User user) {
        Post post = Post.create(
                request.title(),
                request.content(),
                request.boardType() != null ? request.boardType() : BoardType.GENERAL,
                user
        );
        postRepository.save(post);
    }

    /** 게시판별 조회 */
    public List<PostResponseDto> findByBoardType(BoardType boardType) {
        return postRepository.findByBoardType(boardType)
                .stream()
                .map(PostResponseDto::from)
                .toList();
    }

    public List<PostResponseDto> findCommunityPosts() {
        return postRepository.findAll()
                .stream()
                .filter(post -> post.getBoardType() == null || post.getBoardType() == BoardType.GENERAL)
                .map(PostResponseDto::from)
                .toList();
    }

    /** 내가 쓴 글 */
    public List<PostResponseDto> myPosts(User user) {
        return postRepository.findByAuthor(user)
                .stream()
                .map(PostResponseDto::from)
                .toList();
    }

    public PostDetailResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
        return PostDetailResponseDto.from(post);
    }

    /** 삭제 */
    @Transactional
    public void delete(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        if (currentUser.getRole() == Role.ADMIN ||
                post.getAuthor().getId().equals(currentUser.getId())) {
            postRepository.delete(post);
            return;
        }

        throw new AccessDeniedException("게시글 삭제 권한이 없습니다.");
    }
}
