package com.quizprez.quizprezpptxparsing.services;

import lombok.extern.slf4j.Slf4j;
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
import java.util.Objects;
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
                    "-i",
                    "-noframes",
                    "-q",
                    pdfFile.toString(),
                    outputDir.resolve("output").toString()
            ).redirectErrorStream(true).start();

            String processOutput = readProcessOutput(process);
            log.atInfo().log("pdftohtml output:\n{}", processOutput);

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("pdftohtml failed with code: " + exitCode + "\nOutput: " + processOutput);
            }

            Path outputHtml = findOutputFile(outputDir);
            return Files.readString(outputHtml, StandardCharsets.UTF_8);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Conversion interrupted", e);
        } finally {
            FileUtils.deleteQuietly(outputDir.toFile());
        }
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

        html = "<!DOCTYPE html><html><body>" + html + "</body></html>";

        return html;
    }

    private void deleteSilently(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }
}
