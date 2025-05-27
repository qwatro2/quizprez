package com.quizprez.quizprezquiz.service.impl;

import com.quizprez.quizprezquiz.config.FrontendConfig;
import com.quizprez.quizprezquiz.dto.CreateSessionRequest;
import com.quizprez.quizprezquiz.dto.CreateSessionResponse;
import com.quizprez.quizprezquiz.entity.QuizEntity;
import com.quizprez.quizprezquiz.model.QuizModel;
import com.quizprez.quizprezquiz.model.QuizSession;
import com.quizprez.quizprezquiz.repository.QuizRepository;
import com.quizprez.quizprezquiz.repository.QuizSessionRepository;
import com.quizprez.quizprezquiz.service.QrCodeService;
import com.quizprez.quizprezquiz.service.QuizEntityToQuizModelConverter;
import com.quizprez.quizprezquiz.service.QuizParser;
import com.quizprez.quizprezquiz.service.QuizSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class QuizSessionServiceImpl implements QuizSessionService {
    private final FrontendConfig frontendConfig;
    private final QrCodeService qrCodeService;
    private final QuizParser quizParser;
    private final QuizEntityToQuizModelConverter quizEntityToQuizModelConverter;
    private final QuizRepository quizRepository;
    private final QuizSessionRepository quizSessionRepository;

    @Override
    public CreateSessionResponse createSession(CreateSessionRequest request) throws Exception {
        QuizEntity quizEntity = quizParser.parse(request.getHtml());
        QuizModel quizModel = quizEntityToQuizModelConverter.convert(quizEntity);
        quizRepository.save(quizModel);

        String code = qrCodeService.generateSessionCode();
        QuizSession quizSession = QuizSession.builder()
                .code(code)
                .quiz(quizModel)
                .build();
        quizSessionRepository.save(quizSession);

        byte[] qrBytes = qrCodeService.generateQrCodeImage(
                participantJoinLink(frontendConfig.baseUrl(), code),
                200,
                200
        );
        String base64 = Base64.getEncoder().encodeToString(qrBytes);

        return new CreateSessionResponse(code, base64);
    }

    private String participantJoinLink(String baseUrl, String code) {
        return String.format("%s/quiz/join?code=%s", baseUrl, code);
    }
}
