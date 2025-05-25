package com.quizprez.quizprezquiz.controller.v1;

import com.quizprez.quizprezquiz.dto.ParticipantJoinRequest;
import com.quizprez.quizprezquiz.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/participant")
@RequiredArgsConstructor
public class ParticipantController {
    private final QuizSessionService sessionService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody ParticipantJoinRequest req) {
        try {
            sessionService.joinSession(req);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
