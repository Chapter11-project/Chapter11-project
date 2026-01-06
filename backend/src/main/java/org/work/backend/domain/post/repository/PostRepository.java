package org.work.backend.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.work.backend.domain.post.BoardType;
import org.work.backend.domain.post.Post;
import org.work.backend.domain.user.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 페이징 지원: 유저 글 조회
    Page<Post> findByAuthor(User author, Pageable pageable);

    // 리스트 반환: 필요 시
    List<Post> findByAuthor(User author);

    // 게시판별 조회
    List<Post> findByBoardTypeOrderByCreatedAtDesc(BoardType boardType);

    List<Post> findByAuthorOrderByCreatedAtDesc(User author);
}
