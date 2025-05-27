package com.quizprez.quizprezquiz.service;

import com.quizprez.quizprezquiz.dto.CreateSessionRequest;
import com.quizprez.quizprezquiz.dto.CreateSessionResponse;
import com.quizprez.quizprezquiz.model.QuizSession;

import java.util.Optional;

public interface QuizSessionService {
    CreateSessionResponse createSession(CreateSessionRequest request) throws Exception;

    Optional<QuizSession> findByCode(String code);
}
