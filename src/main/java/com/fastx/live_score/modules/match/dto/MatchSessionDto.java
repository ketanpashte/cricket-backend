package com.fastx.live_score.modules.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchSessionDto {
    private Long id;
    private Long matchId;

    // Team 1 Rate
    private Double team1Min;
    private Double team1Max;

    // Team 2 Rate
    private Double team2Min;
    private Double team2Max;

    // Draw Rate
    private Double drawMin;
    private Double drawMax;

    // Session Score
    private Integer sessionOver;
    private Integer sessionMinScore;
    private Integer sessionMaxScore;

    // Lambi Score
    private Integer lambiMinScore;
    private Integer lambiMaxScore;
}
