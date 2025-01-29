package com.quizprez.quizprezauth.service;

import com.quizprez.quizprezauth.entity.ConfirmationToken;
import com.quizprez.quizprezauth.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository tokenRepository;
    private final Logger logger = LoggerFactory.getLogger(ConfirmationTokenService.class);

    @Async
    @Scheduled(fixedDelay = 15 * 60 * 1000)
    @Transactional
    public void cleanExpiredTokens() {
        logger.info("Start cleaning expired tokens");
        Set<ConfirmationToken> expiredTokens = tokenRepository.findAllByExpiresAtLessThan(LocalDateTime.now());

        if (!expiredTokens.isEmpty()) {
            tokenRepository.deleteAll(expiredTokens);
            logger.info("Expired tokens were cleaned successfully");
        } else {
            logger.info("No expired tokens found");
        }
    }
}
