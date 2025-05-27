package com.quizprez.quizprezquiz.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class OptionEntity {
    private final String text;
    private final Boolean isCorrect;
    private Map<String, String> styles;

    public OptionEntity(String text, Boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.styles = new HashMap<>();
    }

    public OptionEntity setStyles(Map<String, String> styles) {
        this.styles = styles;
        return this;
    }
}