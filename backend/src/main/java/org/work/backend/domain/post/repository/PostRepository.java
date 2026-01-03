package org.work.backend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.work.backend.domain.post.BoardType;
import org.work.backend.domain.post.Post;
import org.work.backend.domain.user.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthor(User author);

    List<Post> findByBoardType(BoardType boardType);
}
