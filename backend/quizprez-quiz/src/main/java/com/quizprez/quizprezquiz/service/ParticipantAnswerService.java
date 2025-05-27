package com.quizprez.quizprezquiz.service;

import com.quizprez.quizprezquiz.model.Participant;
import com.quizprez.quizprezquiz.model.QuestionModel;
import com.quizprez.quizprezquiz.model.QuizSession;

public interface ParticipantAnswerService {
    boolean checkCorrectness(Long chosenAnswerId, QuestionModel questionModel);

    void save(QuizSession quizSession, Participant participant, QuestionModel questionModel, boolean isCorrect);
}
