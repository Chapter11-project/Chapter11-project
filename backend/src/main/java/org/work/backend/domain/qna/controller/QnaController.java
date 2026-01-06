package org.work.backend.domain.qna.controller;

import org.work.backend.domain.qna.dto.AnswerRequestDto;
import org.work.backend.domain.qna.dto.AnswerResponseDto;
import org.work.backend.domain.qna.dto.QuestionRequestDto;
import org.work.backend.domain.qna.dto.QuestionResponseDto;
import org.work.backend.domain.qna.service.QnaService;
import org.work.backend.domain.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.work.backend.common.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    @PostMapping
    public ApiResponse<QuestionResponseDto> create(
            @RequestBody QuestionRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.ok(qnaService.createQuestion(request, userDetails.getUser()));
    }

    @GetMapping
    public ApiResponse<List<QuestionResponseDto>> list() {
        return ApiResponse.ok(qnaService.list());
    }

    @GetMapping("/{id}")
    public ApiResponse<QuestionResponseDto> detail(@PathVariable Long id) {
        return ApiResponse.ok(qnaService.detail(id));
    }

    @PostMapping("/{questionId}/answers")
    public ApiResponse<AnswerResponseDto> answer(
            @PathVariable Long questionId,
            @RequestBody AnswerRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.ok(qnaService.createAnswer(questionId, request, userDetails.getUser()));
    }

    @PostMapping("/{questionId}/answers/{answerId}/accept")
    public ApiResponse<Void> accept(
            @PathVariable Long questionId,
            @PathVariable Long answerId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        qnaService.acceptAnswer(questionId, answerId, userDetails.getUser());
        return ApiResponse.ok();
    }
}
