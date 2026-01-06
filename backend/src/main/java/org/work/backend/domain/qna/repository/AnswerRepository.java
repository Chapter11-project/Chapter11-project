package org.work.backend.domain.qna.repository;

import org.work.backend.domain.qna.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdAndQuestionId(Long id, Long questionId);
    Optional<Answer> findByQuestionIdAndIsAcceptedTrue(Long questionId);
}
