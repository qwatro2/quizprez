package com.quizprez.quizprezquiz.repository;

import com.quizprez.quizprezquiz.model.QuestionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionModel, Long> {
}
