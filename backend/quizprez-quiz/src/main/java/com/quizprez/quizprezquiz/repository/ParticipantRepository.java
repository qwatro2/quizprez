package com.quizprez.quizprezquiz.repository;

import com.quizprez.quizprezquiz.model.Participant;
import com.quizprez.quizprezquiz.model.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsParticipantByQuizSessionAndName(QuizSession session, String name);
}
