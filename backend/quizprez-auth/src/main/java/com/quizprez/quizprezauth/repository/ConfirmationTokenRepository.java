package com.quizprez.quizprezauth.repository;

import com.quizprez.quizprezauth.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
    Set<ConfirmationToken> findAllByExpiresAtLessThan(LocalDateTime localDateTime);
}
