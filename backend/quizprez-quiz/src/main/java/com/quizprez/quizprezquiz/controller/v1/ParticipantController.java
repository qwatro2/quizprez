package com.quizprez.quizprezquiz.controller.v1;

import com.quizprez.quizprezquiz.dto.ParticipantAnswerRequest;
import com.quizprez.quizprezquiz.dto.ParticipantJoinRequest;
import com.quizprez.quizprezquiz.dto.ParticipantJoinResponse;
import com.quizprez.quizprezquiz.model.*;
import com.quizprez.quizprezquiz.repository.QuizRepository;
import com.quizprez.quizprezquiz.service.ParticipantAnswerService;
import com.quizprez.quizprezquiz.service.ParticipantService;
import com.quizprez.quizprezquiz.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/participant")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;
    private final QuizSessionService quizSessionService;
    private final ParticipantAnswerService participantAnswerService;
    private final QuizRepository quizRepository;

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

    @PutMapping("/answer")
    public ResponseEntity<?> answer(@RequestBody ParticipantAnswerRequest req) {
        Optional<QuizSession> optionalQuizSession = quizSessionService.findByCode(req.sessionCode());
        if (optionalQuizSession.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid session code");
        }
        QuizSession quizSession = optionalQuizSession.get();
        if (!participantService.existsParticipant(optionalQuizSession.get(), req.participantName())) {
            return ResponseEntity.badRequest().body(String.format("No participant with name %s in session", req.participantName()));
        }
        if (!Objects.equals(quizSession.getQuiz().getId(), req.quizId())) {
            return ResponseEntity.badRequest().body("Invalid quiz ID");
        }

        QuizModel quizModel = quizRepository.findQuizModelById(req.quizId()).get();
        if (quizModel.getQuestionModels()
                .stream().map(QuestionModel::getId)
                .noneMatch(qmid -> Objects.equals(qmid, req.questionId()))) {
            return ResponseEntity.badRequest().body("Invalid question ID");
        }
        QuestionModel questionModel = quizModel.getQuestionModels().stream()
                .filter(qm -> Objects.equals(qm.getId(), req.questionId()))
                .findFirst().get();

        if (questionModel.getOptionModels()
                .stream().map(OptionModel::getId)
                .noneMatch(omid -> Objects.equals(omid, req.chosenAnswerId()))) {
            return ResponseEntity.badRequest().body("Invalid answer ID");
        }

        boolean isCorrect = participantAnswerService.checkCorrectness(req.chosenAnswerId(), questionModel);
        participantAnswerService.save(quizSession,
                participantService.findParticipant(quizSession, req.participantName()),
                questionModel,
                isCorrect);
        return ResponseEntity.ok(isCorrect);
    }
}
