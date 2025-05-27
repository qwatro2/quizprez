package com.quizprez.quizprezquiz.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record ResultsTable(
        Long quizSessionId,
        Map<Long, ParticipantResults> participantResultsMap
) {
}
