package com.quizprez.quizprezauth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "frontend")
@Getter
@Setter
public class FrontendProperties {
    private String protocol;
    private String host;
    private String port;
}
