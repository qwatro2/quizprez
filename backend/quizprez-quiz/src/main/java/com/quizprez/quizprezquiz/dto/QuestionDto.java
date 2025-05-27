package com.quizprez.quizprezquiz.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record QuestionDto(
        Long id,
        String text,
        Long timeSeconds,
        String styles,
        List<OptionDto> optionDtos
) {
}
