package com.quizprez.quizprezquiz.dto;

import lombok.Builder;

@Builder
public record ParticipantJoinResponse(
        String sessionCode,
        String participantName,
        QuizDto quizDto
) {
}
