package com.quizprez.quizprezpresentation.service.impl;

import com.quizprez.quizprezpresentation.service.CustomHtmlConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ConditionalOnProperty(prefix = "app", name = "custom-html-converter", havingValue = "identical")
public class IdenticalCustomHtmlConverterImpl implements CustomHtmlConverter {
    @Override
    public String convert(String customHtml, Map<String, Object> options) {
        return customHtml;
    }
}
