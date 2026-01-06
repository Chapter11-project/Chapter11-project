package org.work.backend.domain.qna.service;

import org.work.backend.domain.notification.service.NotificationService;
import org.work.backend.domain.qna.Answer;
import org.work.backend.domain.qna.Question;
import org.work.backend.domain.qna.QuestionStatus;
import org.work.backend.domain.qna.dto.AnswerRequestDto;
import org.work.backend.domain.qna.dto.AnswerResponseDto;
import org.work.backend.domain.qna.dto.QuestionRequestDto;
import org.work.backend.domain.qna.dto.QuestionResponseDto;
import org.work.backend.domain.qna.repository.AnswerRepository;
import org.work.backend.domain.qna.repository.QuestionRepository;
import org.work.backend.domain.user.User;
import org.work.backend.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final NotificationService notificationService;

    @Transactional
    public QuestionResponseDto createQuestion(QuestionRequestDto request, User author) {
        Question question = new Question(request.title(), request.content(), author);
        Question saved = questionRepository.save(question);
        return QuestionResponseDto.from(saved);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponseDto> list() {
        return questionRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(QuestionResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponseDto detail(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다."));
        return QuestionResponseDto.from(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponseDto> findByAuthor(User author) {
        return questionRepository.findByAuthorOrderByCreatedAtDesc(author)
                .stream()
                .map(QuestionResponseDto::from)
                .toList();
    }


    @Transactional
    public AnswerResponseDto createAnswer(Long questionId, AnswerRequestDto request, User author) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다."));
        Answer answer = new Answer(request.content(), author, question);
        Answer saved = answerRepository.save(answer);
        notificationService.notify(
                question.getAuthor(),
                author,
                "내 질문에 새로운 답변이 등록되었습니다.",
                "/qna.html?questionId=" + questionId
        );
        return AnswerResponseDto.from(saved);
    }

    @Transactional
    public void acceptAnswer(Long questionId, Long answerId, User currentUser) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다."));

        if (!question.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("답변 채택 권한이 없습니다.");
        }

        if (question.getStatus() == QuestionStatus.SOLVED) {
            throw new RuntimeException("이미 해결된 질문입니다.");
        }

        Answer answer = answerRepository.findByIdAndQuestionId(answerId, questionId)
                .orElseThrow(() -> new RuntimeException("답변을 찾을 수 없습니다."));

        answer.accept();
        question.updateStatus(QuestionStatus.SOLVED);
        answerRepository.save(answer);
        questionRepository.save(question);
        notificationService.notify(
                answer.getAuthor(),
                currentUser,
                "내 답변이 채택되었습니다!",
                "/qna.html?questionId=" + questionId
        );
    }
}