package com.quizprez.quizprezauth.controller.v1;

import com.quizprez.quizprezauth.config.BackendProperties;
import com.quizprez.quizprezauth.dto.LoginRequest;
import com.quizprez.quizprezauth.dto.RefreshTokenRequest;
import com.quizprez.quizprezauth.dto.RegistrationRequest;
import com.quizprez.quizprezauth.dto.TokenResponse;
import com.quizprez.quizprezauth.entity.ConfirmationToken;
import com.quizprez.quizprezauth.entity.RefreshToken;
import com.quizprez.quizprezauth.entity.User;
import com.quizprez.quizprezauth.exception.TokenExpiredException;
import com.quizprez.quizprezauth.repository.ConfirmationTokenRepository;
import com.quizprez.quizprezauth.repository.UserRepository;
import com.quizprez.quizprezauth.service.MailSenderService;
import com.quizprez.quizprezauth.service.RefreshTokenService;
import com.quizprez.quizprezauth.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final JwtUtil jwtUtil;
    private final MailSenderService mailSenderService;
    private final BackendProperties backendProperties;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.badRequest().body("Неверный email или пароль");
        }

        User user = userOpt.get();
        String accessToken = jwtUtil.generateAccessToken(user.getEmail());

        RefreshToken refreshTokenEntity = refreshTokenService.createRefreshToken(user);
        String refreshToken = refreshTokenEntity.getToken();

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request) {
        Optional<RefreshToken> tokenOpt = refreshTokenService.findByToken(request.getRefreshToken());
        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Невалидный refresh-токен");
        }

        try {
            RefreshToken validRefreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
            User user = validRefreshToken.getUser();
            String newAccessToken = jwtUtil.generateAccessToken(user.getEmail());
            return ResponseEntity.ok(new TokenResponse(newAccessToken, request.getRefreshToken()));
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh-токен истек, требуется повторный вход");
        }
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email уже занят");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);

        ConfirmationToken token = new ConfirmationToken(user);
        confirmationTokenRepository.save(token);

        String confirmationLinkPattern = "{0}/api/auth/confirm?token={1}";
        String confirmationLink = MessageFormat.format(confirmationLinkPattern,
                constructBackendLink(),
                token.getToken());

        mailSenderService.sendMail(request.getEmail(), confirmationLink);

        return ResponseEntity.ok("Письмо с подтверждением отправлено");
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmEmail(@RequestParam String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Токен не найден"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email уже подтвержден");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Токен истек");
        }

        User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);

        return ResponseEntity.ok("Email подтвержден!");
    }


    private String constructBackendLink() {
        return MessageFormat.format("{0}://{1}:{2}",
                backendProperties.getProtocol(),
                backendProperties.getHost(),
                backendProperties.getPort());
    }
}
