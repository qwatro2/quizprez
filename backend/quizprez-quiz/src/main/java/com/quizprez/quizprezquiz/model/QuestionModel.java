package com.quizprez.quizprezquiz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "question_models")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private Long timeSeconds;

    private String styles;

    @OneToMany(mappedBy = "questionModel", cascade = CascadeType.ALL)
    private List<OptionModel> optionModels;

    @ManyToOne
    @JoinColumn(name = "quiz_model_id")
    private QuizModel quizModel;
}
