package com.quizprez.quizprezauth.service;

import com.quizprez.quizprezauth.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository tokenRepository;
    private final Logger logger = LoggerFactory.getLogger(ConfirmationTokenService.class);

    @Async
    @Scheduled(fixedDelay = 15 * 60 * 1000)  // раз в 15 минут
    public void cleanExpiredTokens() {
        logger.info("Start cleaning expired tokens");
        tokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
        logger.info("Expired tokens was cleaned");
    }
}
