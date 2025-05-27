package com.quizprez.quizprezquiz.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record QuizDto(
        Long id,
        String styles,
        List<QuestionDto> questionDtos
) {
}
