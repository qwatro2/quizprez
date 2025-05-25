package com.quizprez.quizprezquiz.websocket;

import com.quizprez.quizprezquiz.dto.AnswerMessage;
import com.quizprez.quizprezquiz.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class QuizWebSocketController {
    private final QuizSessionService service;

    // админ или участник шлёт ответ
    @MessageMapping("/quiz/answer")
    public void handleAnswer(AnswerMessage msg) {
        service.submitAnswer(msg);
    }

    // админ начинает или перезапускает вопрос
    @MessageMapping("/quiz/start")
    public void handleStart(String sessionCode) {
        service.startQuestion(sessionCode);
    }

    // админ переходит к следующему вопросу
    @MessageMapping("/quiz/next")
    public void handleNext(String sessionCode) {
        service.nextQuestion(sessionCode);
    }
}
