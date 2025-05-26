package com.quizprez.quizprezpresentation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "presentations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Presentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(name = "custom_html", nullable = false, columnDefinition = "TEXT")
    private String customHtml;

    @Lob
    @Column(name = "converted_html", columnDefinition = "TEXT")
    private String convertedHtml;
}
