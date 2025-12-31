package org.work.backend.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.work.backend.domain.post.BoardType;
import org.work.backend.domain.post.Post;
import org.work.backend.domain.post.dto.PostRequestDto;
import org.work.backend.domain.post.dto.PostResponseDto;
import org.work.backend.domain.post.repository.PostRepository;
import org.work.backend.domain.user.Role;
import org.work.backend.domain.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /** 글 작성 */
    public void create(PostRequestDto request, User user) {
        Post post = Post.create(
                request.title(),
                request.content(),
                request.boardType(),
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

//    내가 쓴 글 조회 - 페이징
    public Page<PostResponseDto> myPosts(User user, Pageable pageable) {
        return postRepository.findByAuthor(user, pageable)
                .map(PostResponseDto::from); // Page.map 사용
    }

//    내가 쓴 글 조회 - 전체
    public List<PostResponseDto> myPosts(User user) {
        return postRepository.findByAuthor(user)
                .stream()
                .map(PostResponseDto::from)
                .toList();
    }


    /** 삭제 */
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
