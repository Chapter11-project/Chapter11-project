package org.work.backend.domain.qna.dto;

import org.work.backend.domain.qna.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AnswerResponseDto {
    private Long id;
    private String content;
    private String authorUsername;
    private boolean accepted;
    private LocalDateTime createdAt;

    public static AnswerResponseDto from(Answer answer) {
        return new AnswerResponseDto(
                answer.getId(),
                answer.getContent(),
                answer.getAuthor().getUsername(),
                answer.isAccepted(),
                answer.getCreatedAt()
        );
    }
}
