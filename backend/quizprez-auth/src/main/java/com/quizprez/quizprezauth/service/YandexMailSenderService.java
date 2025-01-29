package com.quizprez.quizprezauth.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class YandexMailSenderService implements MailSenderService {
    private final JavaMailSender mailSender;
    private final Environment environment;
    private final Logger logger = LoggerFactory.getLogger(YandexMailSenderService.class);

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
            logger.error(e.getMessage());
            throw e;
        }
    }
}
