package com.quizprez.quizprezquiz.service.impl;

import com.quizprez.quizprezquiz.service.StylesConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StylesConverterImpl implements StylesConverter {
    @Override
    public Map<String, String> expand(String stylesString) {
        Map<String, String> map = new LinkedHashMap<>();
        if (stylesString == null || stylesString.isBlank()) {
            return map;
        }
        String[] declarations = stylesString.split(";");
        for (String decl : declarations) {
            String trimmed = decl.trim();
            if (trimmed.isEmpty()) continue;
            String[] kv = trimmed.split(":", 2);
            if (kv.length == 2) {
                map.put(kv[0].trim(), kv[1].trim());
            }
        }
        return map;
    }

    @Override
    public String collapse(Map<String, String> stylesMap) {
        List<String> list = new ArrayList<>();
        StringBuilder sb;
        for (Map.Entry<String, String> entry : stylesMap.entrySet()) {
            sb = new StringBuilder();
            sb.append("\"").append(entry.getKey()).append("\"").append(":")
                    .append("\"").append(entry.getValue()).append("\"");
            list.add(sb.toString());
        }
        return String.join(";", list);
    }
}
