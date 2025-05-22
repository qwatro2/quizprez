package com.quizprez.quizprezpptxparsing.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jodconverter.core.util.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@ConditionalOnProperty(name = "conversation-method", havingValue = "pdftohtml")
public class PdftohtmlPresentationConverterService implements PresentationConverterService {
    @Override
    public String convert(MultipartFile file) throws IOException {
        try {
            Path pdf = convertToPdf(file);
            String html = convertPdfToHtml(pdf);
            return postProcessHtml(html);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Conversion interrupted", e);
        }
    }

    private Path convertToPdf(MultipartFile file) throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("pdf-conv-");
        Path inputFile = tempDir.resolve(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            file.transferTo(inputFile);

            Process process = new ProcessBuilder(
                    "libreoffice",
                    "--headless",
                    "--convert-to", "pdf",
                    "--outdir", tempDir.toString(),
                    inputFile.toString()
            ).start();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("PDF conversion failed");
            }

            return tempDir.resolve(inputFile.getFileName().toString().replaceAll("\\..+$", ".pdf"));

        } finally {
            Files.walk(tempDir)
                    .filter(path -> !path.toString().endsWith(".pdf"))
                    .forEach(this::deleteSilently);
        }
    }

    private String convertPdfToHtml(Path pdfFile) throws IOException {
        Path outputDir = Files.createTempDirectory("html-output-");
        try {
            Process process = new ProcessBuilder(
                    "pdftohtml",
                    "-c",
                    "-s",
                    "-noframes",
                    "-q",
                    "-enc", "UTF-8",
                    "-nodrm",
                    pdfFile.toString(),
                    outputDir.resolve("output").toString()
            ).redirectErrorStream(true).start();

            String processOutput = readProcessOutput(process);
            log.info("pdftohtml output:\n{}", processOutput);

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("pdftohtml failed with code: " + exitCode + "\nOutput: " + processOutput);
            }

            Path outputHtml = findOutputFile(outputDir);
            String htmlContent = Files.readString(outputHtml, StandardCharsets.UTF_8);

            htmlContent = embedImagesAsBase64(htmlContent, outputDir);

            return htmlContent;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Conversion interrupted", e);
        } finally {
            FileUtils.deleteQuietly(outputDir.toFile());
        }
    }

    private String embedImagesAsBase64(String html, Path resourceDir) {
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

    private String readProcessOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private Path findOutputFile(Path dir) throws IOException {
        try (Stream<Path> stream = Files.list(dir)) {
            return stream
                    .filter(p -> p.toString().endsWith(".html"))
                    .findFirst()
                    .orElseThrow(() -> new IOException("No HTML files generated in: " + dir));
        }
    }

    private String postProcessHtml(String html) {
        html = html
                .replaceAll("<meta[^>]+>", "")
                .replaceAll("<title>[^<]+</title>", "")
                .replaceAll("<a name=\"outline\"></a>", "")
                .replaceAll("<h1>Document Outline</h1>", "")
                .replaceAll("<ul>[\\s\\S]*?</ul>", "");

        html = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head><body>"
                + html.replaceAll("<head>", "<head><meta charset=\"UTF-8\">")
                + "</body></html>";

        html = html.replace("\uFEFF", "");

        return html;
    }

    private void deleteSilently(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }
}
