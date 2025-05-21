package com.quizprez.quizprezpptxparsing.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LibreOfficePresentationConverterService implements PresentationConverterService {
    @PostConstruct
    private void checkLibreOfficeInstalled() {
        try {
            Process process = new ProcessBuilder("libreoffice", "--version").start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IllegalStateException("LibreOffice is not installed or not accessible");
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Failed to check LibreOffice installation: " + e.getMessage(), e);
        }
    }

    @Override
    public String convert(MultipartFile file) throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("upload-");
        Path outputDir = Files.createTempDirectory("output-");

        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IllegalArgumentException("Invalid file name");
            }

            Path inputFile = tempDir.resolve(originalFilename);
            Files.copy(file.getInputStream(), inputFile, StandardCopyOption.REPLACE_EXISTING);

            List<String> command = List.of(
                    "libreoffice",
                    "--headless",
                    "--convert-to",
                    "html",
                    inputFile.toAbsolutePath().toString(),
                    "--outdir",
                    outputDir.toAbsolutePath().toString()
            );

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            log.atInfo().log("Executing command: {}", String.join(" ", command));

            Process process = processBuilder.start();
            readProcessOutput(process);

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Conversion failed with exit code: " + exitCode);
            }

            return findAndReadHtmlFile(outputDir);
        } finally {
            cleanup(tempDir);
            cleanup(outputDir);
        }
    }

    private void readProcessOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.atInfo().log(line);
            }
        }
    }

    private String findAndReadHtmlFile(Path outputDir) throws IOException {
        Optional<Path> htmlFile = Files.walk(outputDir)
                .filter(path -> path.toString().endsWith(".html"))
                .findFirst();

        if (htmlFile.isEmpty()) {
            throw new IOException("No HTML file generated");
        }
        return Files.readString(htmlFile.get());
    }

    private void cleanup(Path path) {
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .forEach(this::deleteFile);
        } catch (IOException e) {
            log.atWarn().log("Failed to cleanup directory: {}", path, e);
        }
    }

    private void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            log.atWarn().log("Failed to delete file: {}", path, e);
        }
    }
}
