package org.work.backend.domain.qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.work.backend.domain.qna.Question;
import org.work.backend.domain.user.CustomUserDetails;
import org.work.backend.domain.user.User;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByOrderByCreatedAtDesc();

    List<Question> findByAuthorOrderByCreatedAtDesc(User author);
}