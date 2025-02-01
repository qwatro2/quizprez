package com.quizprez.quizprezauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class QuizprezAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizprezAuthApplication.class, args);
    }

}
