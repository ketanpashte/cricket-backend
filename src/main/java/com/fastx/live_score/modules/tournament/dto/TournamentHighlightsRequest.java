package com.fastx.live_score.modules.tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentHighlightsRequest {
    private String highlightMostRunsPlayer;
    private String highlightMostRunsValue;
    private String highlightMostWicketsPlayer;
    private String highlightMostWicketsValue;
    private String highlightBestFigurePlayer;
    private String highlightBestFigureValue;
}
