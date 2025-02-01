package com.quizprez.quizprezauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class YandexMailSenderService implements MailSenderService {
    private final JavaMailSender mailSender;
    private final Environment environment;

    @Override
    public void sendMail(String email, String confirmationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Подтверждение регистрации");
        message.setText("Для подтверждения перейдите по ссылке: " + confirmationLink);
        message.setFrom(MessageFormat.format("{0}@yandex.ru", environment.getProperty("MAIL_USERNAME")));

        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
