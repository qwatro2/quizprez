package com.quizprez.quizprezquiz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frontend")
public record FrontendConfig(String baseUrl) {
}
