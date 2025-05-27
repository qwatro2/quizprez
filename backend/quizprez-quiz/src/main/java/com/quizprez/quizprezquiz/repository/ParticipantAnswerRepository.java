package com.quizprez.quizprezquiz.repository;

import com.quizprez.quizprezquiz.model.ParticipantAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantAnswerRepository extends JpaRepository<ParticipantAnswer, Long> {
    List<ParticipantAnswer> findByQuizSessionId(Long sessionId);
}
