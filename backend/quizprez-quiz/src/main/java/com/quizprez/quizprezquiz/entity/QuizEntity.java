package com.quizprez.quizprezquiz.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class QuizEntity {
    private final List<QuestionEntity> questionEntities;
    private Map<String, String> styles;

    public QuizEntity() {
        questionEntities = new ArrayList<>();
        styles = new HashMap<>();
    }

    public void addQuestion(QuestionEntity questionEntity) {
        questionEntities.add(questionEntity);
    }

    public QuizEntity setStyles(Map<String, String> styles) {
        this.styles = styles;
        return this;
    }
}
