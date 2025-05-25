package com.quizprez.quizprezquiz.repository;

import com.quizprez.quizprezquiz.model.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {
    Optional<QuizSession> findByCode(String code);
}
