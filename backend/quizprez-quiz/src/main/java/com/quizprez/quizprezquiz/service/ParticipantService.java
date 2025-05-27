package com.quizprez.quizprezquiz.service;

import com.quizprez.quizprezquiz.dto.ParticipantJoinResponse;
import com.quizprez.quizprezquiz.model.Participant;
import com.quizprez.quizprezquiz.model.QuizSession;

public interface ParticipantService {
    boolean existsParticipant(QuizSession quizSession, String participantName);

    Participant createParticipant(QuizSession quizSession, String participantName);

    Participant findParticipant(QuizSession quizSession, String participantName);

    ParticipantJoinResponse constructResponse(Participant participant);
}
