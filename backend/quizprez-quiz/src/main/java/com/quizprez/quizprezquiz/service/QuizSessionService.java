package com.quizprez.quizprezquiz.service;

import com.quizprez.quizprezquiz.dto.CreateSessionRequest;
import com.quizprez.quizprezquiz.dto.CreateSessionResponse;

public interface QuizSessionService {
    CreateSessionResponse createSession(CreateSessionRequest request) throws Exception;
}
