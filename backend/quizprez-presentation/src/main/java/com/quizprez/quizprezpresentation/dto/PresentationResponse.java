package com.quizprez.quizprezpresentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresentationResponse {
    private Long id;
    private Long ownerId;
    private String title;
    private String customHtml;
    private String convertedHtml;
}
