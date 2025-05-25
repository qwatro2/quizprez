package com.quizprez.quizprezpptxparsing.services;

import com.quizprez.quizprezpptxparsing.configs.ImageEmbeddingConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@EnableConfigurationProperties(ImageEmbeddingConfig.class)
@RequiredArgsConstructor
@ConditionalOnProperty(value = "image-embedding.method", havingValue = "url")
public class LinkImageEmbedder implements ImageEmbedder {
    private final ImageEmbeddingConfig imageEmbeddingConfig;

    @PostConstruct
    private void setUp() throws IOException {
        Files.createDirectories(Path.of("")
                .resolve(imageEmbeddingConfig.volumePath()));
    }

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
                    UUID uuid = UUID.randomUUID();
                    Path newFile = Files.createFile(Path.of("")
                            .resolve(imageEmbeddingConfig.volumePath())
                            .resolve(uuid.toString()));
                    Files.copy(imgFile, newFile, StandardCopyOption.REPLACE_EXISTING);
                    String replacement = "<img src=\"" + imageEmbeddingConfig.urlPath() + "/" + uuid + "\"";
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
}
