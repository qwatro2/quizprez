package com.quizprez.quizprezquiz.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class QuestionEntity {
    private final String text;
    private final String time;
    private final List<OptionEntity> options;
    private Map<String, String> styles;

    public QuestionEntity(String text, String time) {
        this.text = text;
        this.time = time;
        this.options = new ArrayList<>();
        this.styles = new HashMap<>();
    }

    public void addOption(OptionEntity optionEntity) {
        options.add(optionEntity);
    }

    public QuestionEntity setStyles(Map<String, String> styles) {
        this.styles = styles;
        return this;
    }
}