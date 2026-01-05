package org.work.backend.domain.comment.repository;

import org.work.backend.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.work.backend.domain.user.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthor(User author);
}
