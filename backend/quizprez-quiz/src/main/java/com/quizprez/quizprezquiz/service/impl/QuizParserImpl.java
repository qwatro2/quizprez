package com.quizprez.quizprezquiz.service.impl;

import com.quizprez.quizprezquiz.entity.OptionEntity;
import com.quizprez.quizprezquiz.entity.QuestionEntity;
import com.quizprez.quizprezquiz.entity.QuizEntity;
import com.quizprez.quizprezquiz.service.QuizParser;
import com.quizprez.quizprezquiz.service.StylesConverter;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Map;

/*
Парсит квиз из формата
<quiz style="...">
    <question style="..." time="30s">
        <text style="...">текст первого вопроса</text>
        <option>первый вариант ответа на первый вопрос</option>
        <option correct="true">второй вариант ответа на первый вопрос</option>
        <option style="...">третий вариант ответа на первый вопрос</option>
        <option>четвертый вариант ответа на первый вопрос</option>
    </question>
    <question>
        <text>текст второго вопроса</text>
        <option>первый вариант ответа на второй вопрос</option>
        <option correct="true">второй вариант ответа на второй вопрос</option>
        <option>третий вариант ответа на второй вопрос</option>
        <option>четвертый вариант ответа на второй вопрос</option>
    </question>
</quiz>
 */
@Service
@RequiredArgsConstructor
public class QuizParserImpl implements QuizParser {
    private final StylesConverter stylesConverter;

    @Override
    public QuizEntity parse(String html) {
        Document doc = Jsoup.parse(html);
        Element quizEl = doc.selectFirst("quiz");
        if (quizEl == null) {
            throw new IllegalArgumentException("No <quiz> element found in HTML");
        }

        Map<String, String> quizStyles = stylesConverter.expand(quizEl.attr("style"));
        QuizEntity quizEntity = new QuizEntity().setStyles(quizStyles);

        Elements questionEls = quizEl.select("question");
        for (Element qEl : questionEls) {
            String time = qEl.hasAttr("time") ? qEl.attr("time") : null;
            Map<String, String> qStyles = stylesConverter.expand(qEl.attr("style"));
            Element textEl = qEl.selectFirst("text");
            String questionText = textEl != null ? textEl.text() : "";
            QuestionEntity questionEntity = new QuestionEntity(questionText, time).setStyles(qStyles);

            Elements optionEls = qEl.select("option");
            for (Element oEl : optionEls) {
                String optText = oEl.text();
                Boolean isCorrect = oEl.hasAttr("correct")
                        ? Boolean.parseBoolean(oEl.attr("correct"))
                        : Boolean.FALSE;
                Map<String, String> oStyles = stylesConverter.expand(oEl.attr("style"));
                OptionEntity optionEntity = new OptionEntity(optText, isCorrect).setStyles(oStyles);
                questionEntity.addOption(optionEntity);
            }

            quizEntity.addQuestion(questionEntity);
        }

        return quizEntity;
    }
}
