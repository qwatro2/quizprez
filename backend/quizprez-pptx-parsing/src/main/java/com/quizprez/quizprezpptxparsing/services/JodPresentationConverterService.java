package com.quizprez.quizprezpptxparsing.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.util.FileUtils;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@ConditionalOnProperty(name = "conversation-method", havingValue = "jod")
public class JodPresentationConverterService implements PresentationConverterService {
    private final LocalOfficeManager officeManager;
    private final DocumentFormat htmlFormat;

    public JodPresentationConverterService() {
        this.officeManager = LocalOfficeManager.builder()
                .install()
                .build();
        this.htmlFormat = DocumentFormat.builder()
                .from(DefaultDocumentFormatRegistry.HTML)
                .loadProperty("EmbedImages", true)
                .loadProperty("ExportImagesAsBase64", true)
                .loadProperty("ExportStyles", true)
                .loadProperty("ExportBackgrounds", true)
                .build();
    }

    @PostConstruct
    private void startOfficeManager() throws OfficeException {
        officeManager.start();
    }

    @Override
    public String convert(MultipartFile file) throws IOException, OfficeException {
        Path inputDir = null;
        Path outputDir = null;

        try {
            inputDir = Files.createTempDirectory("input-");
            Path inputFile = Files.createTempFile(inputDir, "presentation-", "");
            file.transferTo(inputFile);

            outputDir = Files.createTempDirectory("output-");
            Path outputFile = Files.createTempFile(outputDir, "html-", "");

            LocalConverter.builder()
                    .officeManager(officeManager)
                    .build()
                    .convert(inputFile.toFile())
                    .to(outputFile.toFile())
                    .as(htmlFormat)
                    .execute();

            return processHtmlContent(outputFile, outputDir);
        } finally {
            if (inputDir != null) {
                FileUtils.deleteQuietly(inputDir.toFile());
            }
            if (outputDir != null) {
                FileUtils.deleteQuietly(outputDir.toFile());
            }
        }
    }

    private String processHtmlContent(Path htmlFile, Path resourceDir) throws IOException {
        String htmlContent = Files.readString(htmlFile, StandardCharsets.UTF_8);

        htmlContent = inlineCss(htmlContent, resourceDir);

        htmlContent = htmlContent
                .replaceAll("<meta.*?>", "")
                .replaceAll("<script.*?</script>", "");

        return htmlContent;
    }

    private String inlineCss(String html, Path resourceDir) throws IOException {
        Pattern pattern = Pattern.compile("<link[^>]+href=[\"']([^\"']+.css)[\"']");
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            String cssPath = matcher.group(1);
            Path cssFile = resourceDir.resolve(cssPath);
            if (Files.exists(cssFile)) {
                String cssContent = Files.readString(cssFile);
                html = html.replace(matcher.group(0), "<style>" + cssContent + "</style>");
            }
        }
        return html;
    }

    @PreDestroy
    public void shutdown() throws OfficeException {
        officeManager.stop();
    }
}
