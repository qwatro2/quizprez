package com.quizprez.quizprezquiz.service;

public interface QrCodeService {
    byte[] generateQrCodeImage(String text, int width, int height) throws Exception;

    String generateSessionCode();
}
