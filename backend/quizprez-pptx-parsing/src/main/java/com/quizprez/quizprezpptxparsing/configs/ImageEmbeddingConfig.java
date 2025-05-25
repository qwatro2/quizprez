package com.quizprez.quizprezpptxparsing.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "image-embedding")
public record ImageEmbeddingConfig(String method,
                                   String volumePath,
                                   String urlPath) {
}
