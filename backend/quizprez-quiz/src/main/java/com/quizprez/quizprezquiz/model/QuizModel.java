package com.quizprez.quizprezquiz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "quiz_models")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String styles;

    @OneToMany(mappedBy = "quizModel", cascade = CascadeType.ALL)
    private List<QuestionModel> questionModels;
}
