package com.quizprez.quizprezauth.repository;

import com.quizprez.quizprezauth.entity.RefreshToken;
import com.quizprez.quizprezauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    int deleteByUser(User user);
}
