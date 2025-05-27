package com.quizprez.quizprezquiz.service;

import com.quizprez.quizprezquiz.entity.QuizEntity;
import com.quizprez.quizprezquiz.model.QuizModel;

public interface QuizEntityToQuizModelConverter {
    QuizModel convert(QuizEntity quizEntity);
}
