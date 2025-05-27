package com.quizprez.quizprezpresentation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "quiz-service")
public record QuizServiceConfig(String baseUrl) {
}
