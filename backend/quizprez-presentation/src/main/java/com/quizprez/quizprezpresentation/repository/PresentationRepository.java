package com.quizprez.quizprezpresentation.repository;

import com.quizprez.quizprezpresentation.model.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresentationRepository extends JpaRepository<Presentation, Long> {
    List<Presentation> findByOwnerId(Long ownerId);
}
