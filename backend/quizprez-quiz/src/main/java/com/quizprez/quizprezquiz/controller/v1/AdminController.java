package com.quizprez.quizprezquiz.controller.v1;

import com.quizprez.quizprezquiz.dto.CreateSessionRequest;
import com.quizprez.quizprezquiz.dto.CreateSessionResponse;
import com.quizprez.quizprezquiz.dto.ResultsTable;
import com.quizprez.quizprezquiz.model.QuizSession;
import com.quizprez.quizprezquiz.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final QuizSessionService sessionService;

    @PostMapping("/create")
    public ResponseEntity<CreateSessionResponse> createSession(@RequestBody CreateSessionRequest req) {
        try {
            CreateSessionResponse resp = sessionService.createSession(req.getHtml());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new CreateSessionResponse(null, null));
        }
    }

    @GetMapping("/{code}/results")
    public ResponseEntity<ResultsTable> getResults(@PathVariable String code) {
        Optional<QuizSession> quizSession = sessionService.getSessionByCode(code);
        if (quizSession.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ResultsTable table = sessionService.buildResultsTable(quizSession.get());
        return ResponseEntity.ok(table);
    }
}
