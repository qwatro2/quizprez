package com.quizprez.quizprezpresentation.service;

import java.util.Map;

public interface CustomHtmlConverter {
    String convert(String customHtml, Map<String, Object> options);
}
