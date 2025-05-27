package com.quizprez.quizprezpresentation.service.impl;

import com.quizprez.quizprezpresentation.config.FrontendConfig;
import com.quizprez.quizprezpresentation.config.QuizServiceConfig;
import com.quizprez.quizprezpresentation.service.CustomHtmlConverter;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@EnableConfigurationProperties(QuizServiceConfig.class)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "custom-html-converter", havingValue = "button")
public class ButtonCustomHtmlConverterImpl implements CustomHtmlConverter {
    private final QuizServiceConfig quizServiceConfig;
    private final FrontendConfig frontendConfig;

    @Override
    public String convert(String customHtml, Map<String, Object> options) {
        ButtonMode buttonMode = extractButtonMode(options);

        Document doc = Jsoup.parseBodyFragment(customHtml);
        Elements quizBlocks = doc.select("quiz");

        for (Element quizEl : quizBlocks) {
            String originalQuizHtml = quizEl.outerHtml();
            String escapedQuizHtml = escapeForJs(originalQuizHtml);

            String btnId = "quizBtn_" + UUID.randomUUID().toString().replace("-", "");

            boolean enabled = switch (buttonMode) {
                case DEMO -> true;
                case SAVE, PREVIEW -> false;
            };

            Element button = new Element(Tag.valueOf("button"), "")
                    .attr("id", btnId)
                    .attr("type", "button")
                    .addClass("quiz-button")
                    .text("Start Quiz");
            if (!enabled) {
                button.attr("disabled", "disabled");
            }

            String script = buildScript(btnId, escapedQuizHtml);

            quizEl.replaceWith(button);
            button.after(script);
        }

        return doc.body().html();
    }

    private String escapeForJs(String input) {
        return input
                .replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\r", "")
                .replace("\n", "\\n");
    }

    private String buildScript(String btnId, String escapedQuizHtml) {
        return "<script>\n" +
                "document.addEventListener('DOMContentLoaded', function() {\n" +
                "  var btn = document.getElementById('" + btnId + "');\n" +
                "  btn.addEventListener('click', function() {\n" +
                "    btn.disabled = true;\n" +
                "    fetch('" + quizServiceConfig.baseUrl() + "/admin/create', {\n" +
                "      method: 'POST',\n" +
                "      headers: { 'Content-Type': 'application/json' },\n" +
                "      body: JSON.stringify({ html: '" + escapedQuizHtml + "' })\n" +
                "    })\n" +
                "    .then(function(res) { return res.json(); })\n" +
                "    .then(function(data) {\n" +
                "      var params = new URLSearchParams();\n" +
                "      params.append('code', data.code);\n" +
                "      params.append('base64', data.base64);\n" +
                "      var url = '" + frontendConfig.baseUrl() + "/quiz/creds?' + params.toString();\n" +
                "      if (window.top.top !== window.self) {\n" +
                "        window.top.location.href = url;\n" +
                "      } else {\n" +
                "        window.location.href = url;\n" +
                "      }\n" +
                "    })\n" +
                "    .catch(function(err) {\n" +
                "      console.error(err);\n" +
                "      btn.disabled = false;\n" +
                "    });\n" +
                "  });\n" +
                "});\n" +
                "</script>";
    }

    private ButtonMode extractButtonMode(Map<String, Object> options) {
        if (!options.containsKey("Button Mode") || !(options.get("Button Mode") instanceof ButtonMode)) {
            return ButtonMode.DEMO;
        }
        return (ButtonMode) options.get("Button Mode");
    }
}
