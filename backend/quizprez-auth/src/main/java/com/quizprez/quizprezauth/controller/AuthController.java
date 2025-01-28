package com.quizprez.quizprezauth.controller;

import com.quizprez.quizprezauth.dto.RegistrationRequest;
import com.quizprez.quizprezauth.entity.ConfirmationToken;
import com.quizprez.quizprezauth.entity.User;
import com.quizprez.quizprezauth.repository.ConfirmationTokenRepository;
import com.quizprez.quizprezauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final Environment environment;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
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

        String confirmationLinkPattern = "{0}://{1}:{2}/api/auth/confirm?token={3}";
        String confirmationLink = MessageFormat.format(confirmationLinkPattern,
                environment.getProperty("APP_PROTOCOL"),
                environment.getProperty("APP_HOST"),
                environment.getProperty("APP_PORT"),
                token.getToken());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Подтверждение регистрации");
        message.setText("Для подтверждения перейдите по ссылке: " + confirmationLink);
        message.setFrom(MessageFormat.format("{0}@yandex.ru", environment.getProperty("MAIL_USERNAME")));

        mailSender.send(message);

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
}
