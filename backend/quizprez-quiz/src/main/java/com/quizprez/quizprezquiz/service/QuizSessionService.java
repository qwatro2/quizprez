package com.quizprez.quizprezquiz.service;

import com.quizprez.quizprezquiz.dto.*;
import com.quizprez.quizprezquiz.model.*;
import com.quizprez.quizprezquiz.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class QuizSessionService {
    private final QuizParser quizParser;
    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    private final QuizSessionRepository sessionRepo;
    private final ParticipantRepository participantRepo;
    private final ParticipantAnswerRepository answerRepo;
    private final QrCodeService qrCodeService;
    private final SimpMessagingTemplate ws;

    // Для отслеживания текущего вопроса в сессии
    private final Map<Long, QuestionTracker> trackers = new ConcurrentHashMap<>();

    @Transactional
    public CreateSessionResponse createSession(String html) throws Exception {
        // 1) Парсим HTML → Quiz + вопросы
        Quiz quiz = quizParser.parse(html);
        Quiz finalQuiz = quiz;
        quiz.getQuestions().forEach(q -> q.setQuiz(finalQuiz));
        quiz = quizRepo.save(quiz);

        // 2) Генерируем код + QR
        String code = qrCodeService.generateSessionCode();
        byte[] qrImage = qrCodeService.generateQrCodeImage(code, 200, 200);

        QuizSession session = QuizSession.builder()
                .code(code)
                .quiz(quiz)
                .qrCodeImage(qrImage)
                .build();
        session = sessionRepo.save(session);

        String base64 = Base64.getEncoder().encodeToString(qrImage);
        return new CreateSessionResponse(code, base64);
    }

    @Transactional
    public void joinSession(ParticipantJoinRequest req) {
        QuizSession session = sessionRepo.findByCode(req.getSessionCode())
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        // уникальный имя
        participantRepo.findBySessionAndName(session, req.getName())
                .orElseGet(() -> {
                    Participant p = Participant.builder()
                            .session(session)
                            .name(req.getName())
                            .build();
                    return participantRepo.save(p);
                });
    }

    @Transactional
    public void startQuestion(String sessionCode) {
        QuizSession session = sessionRepo.findByCode(sessionCode)
                .orElseThrow();
        // получаем/создаём трекер
        QuestionTracker t = trackers.computeIfAbsent(session.getId(), id -> new QuestionTracker());
        List<Question> questions = session.getQuiz().getQuestions();
        if (t.index >= questions.size()) {
            broadcastResults(session);
            return;
        }
        Question q = questions.get(t.index);
        t.startedAt = Instant.now();
        t.timeLimitSec = q.getTimeLimitSec();

        QuestionMessage msg = new QuestionMessage(q.getId(), q.getHtml(), q.getTimeLimitSec());
        ws.convertAndSend("/topic/quiz/" + sessionCode + "/question", msg);
    }

    @Transactional
    public void submitAnswer(AnswerMessage am) {
        QuizSession session = sessionRepo.findByCode(am.getSessionCode()).orElseThrow();
        Participant p = participantRepo.findBySessionAndName(session, am.getParticipantName())
                .orElseThrow();
        QuestionTracker t = trackers.get(session.getId());
        Question q = questionRepo.findById(am.getQuestionId()).orElseThrow();
        Instant now = Instant.now();
        if (t.timeLimitSec != null && now.isAfter(t.startedAt.plusSeconds(t.timeLimitSec))) {
            return;
        }
        boolean correct = q.getCorrectAnswer().equals(am.getAnswer());
        ParticipantAnswer pa = ParticipantAnswer.builder()
                .session(session)
                .participant(p)
                .question(q)
                .answer(am.getAnswer())
                .correct(correct)
                .submittedAt(now)
                .build();
        answerRepo.save(pa);
    }

    @Transactional
    public void nextQuestion(String sessionCode) {
        QuizSession session = sessionRepo.findByCode(sessionCode).orElseThrow();
        trackers.computeIfPresent(session.getId(), (k, t) -> {
            t.index++;
            return t;
        });
        startQuestion(sessionCode);
    }

    public ResultsTable buildResultsTable(QuizSession session) {
        List<ParticipantAnswer> answers = answerRepo.findBySessionId(session.getId());
        List<Question> questions = session.getQuiz().getQuestions();
        List<Participant> parts = participantRepo.findBySession(session);

        String[] names = parts.stream().map(Participant::getName).toArray(String[]::new);
        Long[] qIds = questions.stream().map(Question::getId).toArray(Long[]::new);
        String[][] table = new String[parts.size()][questions.size()];

        for (int i = 0; i < parts.size(); i++) {
            for (int j = 0; j < questions.size(); j++) {
                int finalI = i;
                int finalJ = j;
                boolean ok = answers.stream().anyMatch(pa ->
                        pa.getParticipant().equals(parts.get(finalI)) &&
                                pa.getQuestion().equals(questions.get(finalJ)) &&
                                pa.isCorrect()
                );
                table[i][j] = ok ? "1" : "0";
            }
        }
        return new ResultsTable(names, qIds, table);
    }

    public Optional<QuizSession> getSessionByCode(String code) {
        return sessionRepo.findByCode(code);
    }

    private void broadcastResults(QuizSession session) {
        ResultsTable table = buildResultsTable(session);
        ws.convertAndSend("/topic/quiz/" + session.getCode() + "/results", table);
    }

    private static class QuestionTracker {
        int index = 0;
        Instant startedAt;
        Integer timeLimitSec;
    }
}
