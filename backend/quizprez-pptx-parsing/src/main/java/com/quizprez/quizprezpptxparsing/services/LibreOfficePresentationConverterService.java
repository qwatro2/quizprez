package com.quizprez.quizprezpptxparsing.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@ConditionalOnProperty(name = "conversation-method", havingValue = "libre-office")
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

            Path htmlFile = findHtmlFile(outputDir)
                    .orElseThrow(() -> new IOException("No HTML file generated"));
            return processHtmlContent(htmlFile);
        } finally {
            cleanup(tempDir);
            cleanup(outputDir);
        }
    }

    private String processHtmlContent(Path htmlFile) throws IOException {
        String content = Files.readString(htmlFile);
        content = inlineCssStyles(content, htmlFile.getParent());
        content = embedImages(content, htmlFile.getParent());
        content = cleanHtml(content);
        return content;
    }

    private String inlineCssStyles(String html, Path dir) throws IOException {
        Pattern pattern = Pattern.compile("<link.*?href=\"(.*?.css)\".*?>");
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            String cssPath = matcher.group(1);
            Path cssFile = dir.resolve(cssPath);
            if (Files.exists(cssFile)) {
                String cssContent = Files.readString(cssFile);
                html = html.replace(matcher.group(0), "<style>" + cssContent + "</style>");
            }
        }
        return html;
    }

    private String embedImages(String html, Path dir) throws IOException {
        Pattern pattern = Pattern.compile("<img.*?src=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            String imgPath = matcher.group(1);
            Path imgFile = dir.resolve(imgPath);
            if (Files.exists(imgFile)) {
                String mimeType = Files.probeContentType(imgFile);
                byte[] data = Files.readAllBytes(imgFile);
                String base64 = Base64.getEncoder().encodeToString(data);
                String replacement = "data:" + mimeType + ";base64," + base64 + "\"";
                html = html.replace(imgPath + "\"", replacement);
            }
        }
        return html;
    }

    private void readProcessOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.atInfo().log(line);
            }
        }
    }

    private Optional<Path> findHtmlFile(Path outputDir) throws IOException {
        return Files.walk(outputDir)
                .filter(path -> path.toString().endsWith(".html"))
                .findFirst();
    }

    private String cleanHtml(String html) {
        return html
                .replaceAll("<meta.*?>", "")
                .replaceAll("<script.*?</script>", "")
                .replaceAll("<!--.*?-->", "");
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
