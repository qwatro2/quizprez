package com.quizprez.quizprezquiz.repository;

import com.quizprez.quizprezquiz.model.QuizModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<QuizModel, Long> {}
