package com.quizprez.quizprezpptxparsing.services;

import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.office.OfficeException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@ConditionalOnProperty(name = "conversation-method", havingValue = "two-steps")
public class TwoStepsPresentationConverterService implements PresentationConverterService {
    @Override
    public String convert(MultipartFile file) throws IOException, InterruptedException, OfficeException {
        return "";
    }
}
