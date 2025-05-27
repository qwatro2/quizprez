package com.quizprez.quizprezquiz.repository;

import com.quizprez.quizprezquiz.model.QuizModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<QuizModel, Long> {
    Optional<QuizModel> findQuizModelById(Long id);
}
