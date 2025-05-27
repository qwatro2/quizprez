package com.quizprez.quizprezquiz.dto;

public record ParticipantAnswerRequest(
        String participantName,
        String sessionCode,
        Long quizId,
        Long questionId,
        Long chosenAnswerId
) {
}
