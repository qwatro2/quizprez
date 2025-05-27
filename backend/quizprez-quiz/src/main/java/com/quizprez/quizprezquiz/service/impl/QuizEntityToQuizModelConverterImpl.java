package com.quizprez.quizprezquiz.service.impl;

import com.quizprez.quizprezquiz.entity.OptionEntity;
import com.quizprez.quizprezquiz.entity.QuestionEntity;
import com.quizprez.quizprezquiz.entity.QuizEntity;
import com.quizprez.quizprezquiz.model.OptionModel;
import com.quizprez.quizprezquiz.model.QuestionModel;
import com.quizprez.quizprezquiz.model.QuizModel;
import com.quizprez.quizprezquiz.service.QuizEntityToQuizModelConverter;
import com.quizprez.quizprezquiz.service.StylesConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizEntityToQuizModelConverterImpl implements QuizEntityToQuizModelConverter {
    private final StylesConverter stylesConverter;

    @Override
    public QuizModel convert(QuizEntity quizEntity) {
        QuizModel quizModel = QuizModel.builder()
                .styles(stylesConverter.collapse(quizEntity.getStyles()))
                .build();

        List<QuestionModel> questionModels = new ArrayList<>();
        for (QuestionEntity questionEntity : quizEntity.getQuestionEntities()) {
            QuestionModel questionModel = QuestionModel.builder()
                    .text(questionEntity.getText())
                    .timeSeconds(nonNullDurationInSeconds(questionEntity.getTime()))
                    .styles(stylesConverter.collapse(questionEntity.getStyles()))
                    .quizModel(quizModel)
                    .build();

            List<OptionModel> optionModels = new ArrayList<>();
            for (OptionEntity optionEntity : questionEntity.getOptions()) {
                OptionModel optionModel = OptionModel.builder()
                        .text(optionEntity.getText())
                        .isCorrect(optionEntity.getIsCorrect())
                        .styles(stylesConverter.collapse(optionEntity.getStyles()))
                        .questionModel(questionModel)
                        .build();
                optionModels.add(optionModel);
            }
            questionModel.setOptionModels(optionModels);

            questionModels.add(questionModel);
        }

        quizModel.setQuestionModels(questionModels);
        return quizModel;
    }

    private Long nonNullDurationInSeconds(String time) {
        if (time == null) {
            return null;
        }
        try {
            return Duration.parse(time).toSeconds();
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
