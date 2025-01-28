package com.quizprez.quizprezauth.service;

import com.quizprez.quizprezauth.entity.ConfirmationToken;
import com.quizprez.quizprezauth.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository tokenRepository;
    private final Logger logger = LoggerFactory.getLogger(ConfirmationTokenService.class);

    @Scheduled(fixedDelay = 15 * 60 * 1000)  // раз в 15 минут
    public void cleanExpiredTokens() {
        logger.info("Start cleaning expired tokens");
        Set<ConfirmationToken> expiredTokens = tokenRepository.findAllByExpiresAtLessThan(LocalDateTime.now());
        tokenRepository.deleteAll(expiredTokens);
        logger.info("Expired tokens was cleaned");
    }
}
