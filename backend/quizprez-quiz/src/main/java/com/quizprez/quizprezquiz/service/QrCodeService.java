package com.quizprez.quizprezquiz.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

@Service
public class QrCodeService {

    public byte[] generateQrCodeImage(String text, int width, int height) throws Exception {
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(text, BarcodeFormat.QR_CODE, width, height);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
            return baos.toByteArray();
        }
    }

    public String generateSessionCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
