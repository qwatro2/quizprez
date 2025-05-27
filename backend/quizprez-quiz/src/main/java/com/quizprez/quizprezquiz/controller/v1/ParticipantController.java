package com.quizprez.quizprezquiz.controller.v1;

import com.quizprez.quizprezquiz.dto.ParticipantJoinRequest;
import com.quizprez.quizprezquiz.dto.ParticipantJoinResponse;
import com.quizprez.quizprezquiz.model.Participant;
import com.quizprez.quizprezquiz.model.QuizSession;
import com.quizprez.quizprezquiz.service.ParticipantService;
import com.quizprez.quizprezquiz.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/participant")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;
    private final QuizSessionService quizSessionService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody ParticipantJoinRequest req) {
        Optional<QuizSession> optionalQuizSession = quizSessionService.findByCode(req.getSessionCode());
        if (optionalQuizSession.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid session code");
        }
        if (participantService.existsParticipant(optionalQuizSession.get(), req.getName())) {
            return ResponseEntity.badRequest().body("Participant name already used");
        }

        Participant participant = participantService.createParticipant(optionalQuizSession.get(), req.getName());
        ParticipantJoinResponse resp = participantService.constructResponse(participant);
        return ResponseEntity.ok(resp);
    }
}
