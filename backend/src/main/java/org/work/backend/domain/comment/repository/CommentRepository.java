package org.work.backend.domain.comment.repository;

import org.work.backend.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 관리자 삭제 메서드 추가 관련으로 추가
    void deleteByPostId(Long postId);

}