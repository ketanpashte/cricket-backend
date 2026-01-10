package com.fastx.live_score.modules.venue.dto;

import lombok.Data;

@Data
public class VenueScoringDto {
    Integer totalMatches = 0;
    Integer winBatFirst = 0;
    Integer winBowlSecond = 0;
    Integer firstInningBattingAvgScore = 0;
    Integer secondInningBattingAvgScore = 0;
    Integer highestTotal = 0;
}
