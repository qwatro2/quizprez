package com.quizprez.quizprezquiz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
    @JoinColumn(name = "session_id")
    private QuizSession session;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    private String answer;
    private boolean correct;
    private Instant submittedAt;
}
