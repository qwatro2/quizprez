package com.quizprez.quizprezquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantJoinRequest {
    private String sessionCode;
    private String name;
}
