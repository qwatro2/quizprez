package com.quizprez.quizprezquiz.repository;

import com.quizprez.quizprezquiz.model.Participant;
import com.quizprez.quizprezquiz.model.ParticipantAnswer;
import com.quizprez.quizprezquiz.model.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantAnswerRepository extends JpaRepository<ParticipantAnswer, Long> {
    List<ParticipantAnswer> findByParticipantAndQuizSession(Participant participant, QuizSession quizSession);
}
