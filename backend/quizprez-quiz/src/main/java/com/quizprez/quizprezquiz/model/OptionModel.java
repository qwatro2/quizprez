package com.quizprez.quizprezquiz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "option_models")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private Boolean isCorrect;

    private String styles;

    @ManyToOne
    @JoinColumn(name = "question_model_id")
    private QuestionModel questionModel;
}
