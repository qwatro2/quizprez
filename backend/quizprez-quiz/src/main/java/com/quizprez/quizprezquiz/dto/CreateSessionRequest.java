package com.quizprez.quizprezquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionRequest {
    /** HTML-код квиза, который будет парситься через QuizParser */
    private String html;
}
