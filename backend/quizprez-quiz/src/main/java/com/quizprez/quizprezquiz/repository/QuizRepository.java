package com.quizprez.quizprezquiz.repository;

import com.quizprez.quizprezquiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {}
