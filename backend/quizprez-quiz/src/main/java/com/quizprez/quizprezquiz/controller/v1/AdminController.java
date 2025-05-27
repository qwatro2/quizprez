package com.quizprez.quizprezquiz.controller.v1;

import com.quizprez.quizprezquiz.dto.CreateSessionRequest;
import com.quizprez.quizprezquiz.dto.CreateSessionResponse;
import com.quizprez.quizprezquiz.dto.ResultsTable;
import com.quizprez.quizprezquiz.model.QuizSession;
import com.quizprez.quizprezquiz.service.QuizSessionService;
import com.quizprez.quizprezquiz.service.ResultTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final QuizSessionService quizSessionService;
    private final ResultTableService resultTableService;

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
        Optional<QuizSession> optionalQuizSession = quizSessionService.findByCode(code);
        if (optionalQuizSession.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid session code");
        }

        ResultsTable resultsTable = resultTableService.constructResultTable(optionalQuizSession.get());
        return ResponseEntity.ok(resultsTable);
    }
}
