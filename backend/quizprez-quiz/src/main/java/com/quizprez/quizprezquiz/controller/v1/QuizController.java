package com.quizprez.quizprezquiz.controller.v1;

import com.google.zxing.WriterException;
import com.quizprez.quizprezquiz.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QrCodeService qrCodeService;

    @PostMapping("/sessions")
    public ResponseEntity<?> createQuizSession() {
        String uniqueCode = "QUIZ1234";
        return ResponseEntity.ok("Квиз-сессия создана с кодом: " + uniqueCode + " (заглушка)");
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinQuiz(@RequestParam String code, @RequestParam String participantName) {
        return ResponseEntity.ok("Участник " + participantName + " подключился к квиз-сессии с кодом " + code + " (заглушка)");
    }

    @GetMapping("/results")
    public ResponseEntity<?> getQuizResults(@RequestParam String code) {
        return ResponseEntity.ok("Результаты квиза с кодом " + code + " (заглушка)");
    }

    @GetMapping("/generateQr")
    public ResponseEntity<?> generateQr(@RequestParam String code, @RequestParam int width, @RequestParam int height) {
        try {
            String base64Qr = qrCodeService.generateQRCodeBase64(code, width, height);
            return ResponseEntity.ok(base64Qr);
        } catch (WriterException | IOException e) {
            return ResponseEntity.status(500).body("Ошибка генерации QR-кода: " + e.getMessage());
        }
    }
}
