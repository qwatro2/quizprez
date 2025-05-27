package com.quizprez.quizprezquiz.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record ParticipantResults(
        Long participantId,
        Map<Long, Boolean> questionIdToResultMap
) {
}
