package com.quizprez.quizprezquiz.service.impl;

import com.quizprez.quizprezquiz.dto.OptionDto;
import com.quizprez.quizprezquiz.dto.ParticipantJoinResponse;
import com.quizprez.quizprezquiz.dto.QuestionDto;
import com.quizprez.quizprezquiz.dto.QuizDto;
import com.quizprez.quizprezquiz.model.*;
import com.quizprez.quizprezquiz.repository.ParticipantRepository;
import com.quizprez.quizprezquiz.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;

    @Override
    public boolean existsParticipant(QuizSession quizSession, String participantName) {
        return participantRepository.existsParticipantByQuizSessionAndName(quizSession, participantName);
    }

    @Override
    public Participant createParticipant(QuizSession quizSession, String participantName) {
        return participantRepository.save(Participant.builder()
                .name(participantName)
                .quizSession(quizSession)
                .build());
    }

    @Override
    public ParticipantJoinResponse constructResponse(Participant participant) {
        return ParticipantJoinResponse.builder()
                .sessionCode(participant.getQuizSession().getCode())
                .participantName(participant.getName())
                .quizDto(convert(participant.getQuizSession().getQuiz()))
                .build();
    }

    private QuizDto convert(QuizModel model) {
        return QuizDto.builder()
                .id(model.getId())
                .styles(model.getStyles())
                .questionDtos(model.getQuestionModels().stream().map(this::convert).toList())
                .build();
    }

    private QuestionDto convert(QuestionModel model) {
        return QuestionDto.builder()
                .id(model.getId())
                .text(model.getText())
                .timeSeconds(model.getTimeSeconds())
                .styles(model.getStyles())
                .optionDtos(model.getOptionModels().stream().map(this::convert).toList())
                .build();
    }

    private OptionDto convert(OptionModel model) {
        return OptionDto.builder()
                .id(model.getId())
                .text(model.getText())
                .styles(model.getStyles())
                .build();
    }
}
