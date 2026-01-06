package org.work.backend.domain.qna.dto;

import org.work.backend.domain.qna.Question;
import org.work.backend.domain.qna.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionResponseDto {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private QuestionStatus status;
    private LocalDateTime createdAt;
    private List<AnswerResponseDto> answers;

    public static QuestionResponseDto from(Question question) {
        return new QuestionResponseDto(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getAuthor().getUsername(),
                question.getStatus(),
                question.getCreatedAt(),
                question.getAnswers().stream()
                        .map(AnswerResponseDto::from)
                        .toList()
        );
    }
}
