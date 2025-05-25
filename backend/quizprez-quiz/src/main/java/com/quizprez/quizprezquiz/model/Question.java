package com.quizprez.quizprezquiz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Lob
    private String html;            // формулировка + варианты

    private String correctAnswer;   // например, value правильного варианта

    private Integer timeLimitSec;   // null — без таймера
}
