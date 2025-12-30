package org.work.backend.domain.post.repository;

import org.work.backend.domain.post.Post;
import org.work.backend.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(User author);

}