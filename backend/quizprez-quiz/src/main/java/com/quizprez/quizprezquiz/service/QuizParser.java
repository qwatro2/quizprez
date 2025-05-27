package com.quizprez.quizprezquiz.service;

import com.quizprez.quizprezquiz.entity.QuizEntity;

public interface QuizParser {
    QuizEntity parse(String html);
}
