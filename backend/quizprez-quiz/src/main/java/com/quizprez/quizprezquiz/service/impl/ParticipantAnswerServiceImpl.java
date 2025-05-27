package com.quizprez.quizprezquiz.service.impl;

import com.quizprez.quizprezquiz.model.*;
import com.quizprez.quizprezquiz.repository.ParticipantAnswerRepository;
import com.quizprez.quizprezquiz.service.ParticipantAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ParticipantAnswerServiceImpl implements ParticipantAnswerService {
    private final ParticipantAnswerRepository participantAnswerRepository;

    @Override
    public boolean checkCorrectness(Long chosenAnswerId, QuestionModel questionModel) {
        if (chosenAnswerId == null) {
            return false;
        }
        return questionModel.getOptionModels().stream().filter(OptionModel::getIsCorrect)
                .findFirst()
                .map(optionModel -> Objects.equals(optionModel.getId(), chosenAnswerId))
                .orElse(false);
    }

    @Override
    public void save(QuizSession quizSession, Participant participant, QuestionModel questionModel, boolean isCorrect) {
        ParticipantAnswer answer = ParticipantAnswer.builder()
                .quizSession(quizSession)
                .participant(participant)
                .question(questionModel)
                .correct(isCorrect)
                .build();
        participantAnswerRepository.save(answer);
    }
}
