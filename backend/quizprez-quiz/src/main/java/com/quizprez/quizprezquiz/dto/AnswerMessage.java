package com.quizprez.quizprezquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerMessage {
    private String sessionCode;
    private String participantName;
    private Long questionId;
    private String answer;
}
