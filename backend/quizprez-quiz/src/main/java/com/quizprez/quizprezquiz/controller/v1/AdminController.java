package com.quizprez.quizprezquiz.controller.v1;

import com.quizprez.quizprezquiz.dto.CreateSessionRequest;
import com.quizprez.quizprezquiz.dto.CreateSessionResponse;
import com.quizprez.quizprezquiz.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final QuizSessionService quizSessionService;

    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestBody CreateSessionRequest req) {
        try {
            CreateSessionResponse resp = quizSessionService.createSession(req);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{code}/results")
    public ResponseEntity<?> getResults(@PathVariable String code) {
        throw new NotImplementedException();
    }
}
