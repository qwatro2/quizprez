package com.quizprez.quizprezquiz.repository;

import com.quizprez.quizprezquiz.model.Participant;
import com.quizprez.quizprezquiz.model.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findBySessionAndName(QuizSession session, String name);

    List<Participant> findBySession(QuizSession session);
}
