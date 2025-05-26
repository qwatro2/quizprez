package com.quizprez.quizprezpptxparsing.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frontend")
public record FrontendConfig(String baseUrl) {
}
