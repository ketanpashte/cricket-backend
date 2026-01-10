package com.fastx.live_score.modules.player.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Player {
    private Long id;

    private String fullName;
    private String shortName;
    private String nationality;

    private String role;
    private String battingStyle;
    private String bowlingStyle;

    private int totalRuns;
    private int totalWickets;
    private int totalMatches;
    private boolean isActive;


}
