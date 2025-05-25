package com.quizprez.quizprezpptxparsing.services;

import java.nio.file.Path;

public interface ImageEmbedder {
    String embed(String html, Path resourceDir);
}
