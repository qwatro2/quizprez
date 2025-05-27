package com.quizprez.quizprezquiz.service.impl;

import com.quizprez.quizprezquiz.dto.ParticipantResults;
import com.quizprez.quizprezquiz.dto.ResultsTable;
import com.quizprez.quizprezquiz.model.Participant;
import com.quizprez.quizprezquiz.model.ParticipantAnswer;
import com.quizprez.quizprezquiz.model.QuizSession;
import com.quizprez.quizprezquiz.repository.ParticipantAnswerRepository;
import com.quizprez.quizprezquiz.service.ResultTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResultTableServiceImpl implements ResultTableService {
    private final ParticipantAnswerRepository participantAnswerRepository;

    @Override
    public ResultsTable constructResultTable(QuizSession quizSession) {
        Map<Long, ParticipantResults> participantResultsMap = new HashMap<>();
        for (Participant participant : quizSession.getParticipants()) {
            Map<Long, Boolean> questionIdToResultMap = new HashMap<>();
            for (ParticipantAnswer answer :
                    participantAnswerRepository.findByParticipantAndQuizSession(participant, quizSession)) {
                questionIdToResultMap.put(answer.getQuestion().getId(), answer.isCorrect());
            }
            ParticipantResults participantResults = ParticipantResults.builder()
                    .participantId(participant.getId())
                    .questionIdToResultMap(questionIdToResultMap)
                    .build();
            participantResultsMap.put(participant.getId(), participantResults);
        }

        return ResultsTable.builder()
                .quizSessionId(quizSession.getId())
                .participantResultsMap(participantResultsMap)
                .build();
    }
}
