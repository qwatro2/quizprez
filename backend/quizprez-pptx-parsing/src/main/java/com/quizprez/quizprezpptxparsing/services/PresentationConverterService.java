package com.quizprez.quizprezpptxparsing.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PresentationConverterService {
    String convert(MultipartFile file) throws IOException, InterruptedException;
}
