package org.work.backend.domain.qna.repository;

import org.work.backend.domain.qna.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
