package com.quizprez.quizprezauth.service;

import com.quizprez.quizprezauth.entity.ConfirmationToken;
import com.quizprez.quizprezauth.entity.User;
import com.quizprez.quizprezauth.repository.ConfirmationTokenRepository;
import com.quizprez.quizprezauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository tokenRepository;

    @Async
    @Scheduled(fixedDelay = 15 * 60 * 1000)
    @Transactional
    public void cleanDisabledUsers() {
        log.info("Start cleaning disabled users");
        Set<User> disabledUsers = userRepository.findAllByEnabledFalse();

        if (!disabledUsers.isEmpty()) {
            boolean deletedFlag = false;
            for (User user: disabledUsers) {
                Set<ConfirmationToken> tokenOpt = tokenRepository.findAllByUser(user);
                if (tokenOpt.isEmpty()) {
                    userRepository.delete(user);
                    deletedFlag = true;
                }
            }
            if (deletedFlag) {
                log.info("Disabled users were cleaned successfully");
            } else {
                log.info("All disabled users have actual confirmation tokens");
            }
        } else {
            log.info("No disabled users found");
        }
    }
}
