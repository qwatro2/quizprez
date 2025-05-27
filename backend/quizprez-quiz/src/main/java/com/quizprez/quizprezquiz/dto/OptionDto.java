package com.quizprez.quizprezquiz.dto;

import lombok.Builder;

@Builder
public record OptionDto(
        Long id,
        String text,
        String styles
) {
}
