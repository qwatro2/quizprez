package com.quizprez.quizprezpptxparsing.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@ConditionalOnProperty(value = "image-embedding.method", havingValue = "base64")
public class Base64ImageEmbedder implements ImageEmbedder {
    @Override
    public String embed(String html, Path resourceDir) {
        Pattern pattern = Pattern.compile("<img[^>]+src=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(html);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String imgPath = matcher.group(1);
            Path imgFile = resourceDir.resolve(imgPath);

            if (Files.exists(imgFile)) {
                try {
                    String mimeType = determineMimeType(imgFile);
                    byte[] imageData = Files.readAllBytes(imgFile);
                    String base64 = Base64.getEncoder().encodeToString(imageData);
                    String replacement = "<img src=\"data:" + mimeType + ";base64," + base64 + "\"";
                    matcher.appendReplacement(result, replacement);
                } catch (IOException e) {
                    log.error("Error processing image {}: {}", imgFile, e.getMessage());
                    matcher.appendReplacement(result, matcher.group(0));
                }
            } else {
                log.warn("Image file not found: {}", imgFile);
                matcher.appendReplacement(result, matcher.group(0));
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private String determineMimeType(Path imgFile) {
        String extension = FilenameUtils.getExtension(imgFile.toString()).toLowerCase();
        return switch (extension) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            default -> "application/octet-stream";
        };
    }

}
