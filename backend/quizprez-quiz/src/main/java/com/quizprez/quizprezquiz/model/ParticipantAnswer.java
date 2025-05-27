package com.quizprez.quizprezquiz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "participant_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_session_id")
    private QuizSession quizSession;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private QuestionModel question;

    private boolean correct;
}
