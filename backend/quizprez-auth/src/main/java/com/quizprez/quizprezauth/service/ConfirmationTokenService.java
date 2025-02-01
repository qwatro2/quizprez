package com.quizprez.quizprezauth.service;

import com.quizprez.quizprezauth.entity.ConfirmationToken;
import com.quizprez.quizprezauth.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository tokenRepository;

    @Async
    @Scheduled(fixedDelay = 15 * 60 * 1000)
    @Transactional
    public void cleanExpiredTokens() {
        log.info("Start cleaning expired tokens");
        Set<ConfirmationToken> expiredTokens = tokenRepository.findAllByExpiresAtLessThan(LocalDateTime.now());

        if (!expiredTokens.isEmpty()) {
            tokenRepository.deleteAll(expiredTokens);
            log.info("Expired tokens were cleaned successfully");
        } else {
            log.info("No expired tokens found");
        }
    }
}
