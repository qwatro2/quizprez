package com.quizprez.quizprezpptxparsing.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFamily;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Primary
public class JodPresentationConverterService implements PresentationConverterService {
    private final LocalOfficeManager officeManager;

    public JodPresentationConverterService() {
        this.officeManager = LocalOfficeManager.builder()
                .install()
                .build();
    }

    @PostConstruct
    private void startOfficeManager() throws OfficeException {
        officeManager.start();
    }

    @Override
    public String convert(MultipartFile file) throws IOException, OfficeException {
        Path inputPath = null;
        try {
            inputPath = Files.createTempFile("input-", "." + getFileExtension(file));
            file.transferTo(inputPath);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            DocumentFormat format =
                    DocumentFormat.builder()
                            .from(DefaultDocumentFormatRegistry.HTML)
                            .storeProperty(DocumentFamily.TEXT, "FilterOptions", "EmbedImages")
                            .build();

            LocalConverter.builder()
                    .officeManager(officeManager)
                    .build()
                    .convert(inputPath.toFile())
                    .to(outputStream)
                    .as(format)
                    .execute();

            return outputStream.toString(StandardCharsets.UTF_8);

        } finally {
            if (inputPath != null) Files.delete(inputPath);
        }
    }

    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) return "";
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex + 1);
    }

    @PreDestroy
    public void shutdown() throws OfficeException {
        officeManager.stop();
    }
}
