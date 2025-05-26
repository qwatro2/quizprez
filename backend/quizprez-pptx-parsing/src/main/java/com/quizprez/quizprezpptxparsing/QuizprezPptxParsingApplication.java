package com.quizprez.quizprezpptxparsing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class QuizprezPptxParsingApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizprezPptxParsingApplication.class, args);
	}

}
