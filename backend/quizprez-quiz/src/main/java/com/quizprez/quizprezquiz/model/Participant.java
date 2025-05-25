package com.quizprez.quizprezquiz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "participants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "name"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    private QuizSession session;

    @Column(nullable = false)
    private String name;
}
