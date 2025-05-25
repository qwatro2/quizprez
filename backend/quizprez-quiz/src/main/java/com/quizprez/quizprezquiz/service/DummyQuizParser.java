package com.quizprez.quizprezquiz.service;

import com.quizprez.quizprezquiz.model.Question;
import com.quizprez.quizprezquiz.model.Quiz;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DummyQuizParser implements QuizParser {
    @Override
    public Quiz parse(String html) {
        Question question1 = Question.builder()
                .html("<div>Question 1</div>")
                .correctAnswer("Answer 1")
                .build();
        Question question2 = Question.builder()
                .html("<div>Question 2</div>")
                .correctAnswer("Answer 2")
                .build();

        return Quiz.builder()
                .title("Dummy quiz")
                .questions(List.of(question1, question2))
                .build();
    }
}
