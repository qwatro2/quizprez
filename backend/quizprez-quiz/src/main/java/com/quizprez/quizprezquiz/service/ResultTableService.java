package com.quizprez.quizprezquiz.service;

import com.quizprez.quizprezquiz.dto.ResultsTable;
import com.quizprez.quizprezquiz.model.QuizSession;

public interface ResultTableService {
    ResultsTable constructResultTable(QuizSession quizSession);
}
