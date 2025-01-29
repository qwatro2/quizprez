package com.quizprez.quizprezauth.service;

public interface MailSenderService {
    void sendMail(String email, String confirmationLink);
}
