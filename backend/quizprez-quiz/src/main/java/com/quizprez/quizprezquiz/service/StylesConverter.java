package com.quizprez.quizprezquiz.service;

import java.util.Map;

public interface StylesConverter {
    Map<String, String> expand(String stylesString);

    String collapse(Map<String, String> stylesMap);
}
