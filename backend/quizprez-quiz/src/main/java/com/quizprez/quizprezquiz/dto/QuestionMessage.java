package com.quizprez.quizprezquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionMessage {
    private Long questionId;
    private String html;
    private Integer timeLimitSec;
}
