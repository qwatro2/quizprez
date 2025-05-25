package com.quizprez.quizprezquiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultsTable {
    private String[] participantNames;
    private Long[] questionIds;
    private String[][] table; // [i][j] = "1" или "0"
}
