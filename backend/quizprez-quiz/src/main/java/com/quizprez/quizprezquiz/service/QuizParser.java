package com.quizprez.quizprezquiz.service;

import com.quizprez.quizprezquiz.model.Quiz;

public interface QuizParser {
    Quiz parse(String html);
}
