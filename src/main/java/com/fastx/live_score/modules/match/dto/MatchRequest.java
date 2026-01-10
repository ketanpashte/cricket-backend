package com.fastx.live_score.modules.match.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequest {

    private Long matchId;

    private Long tournamentId;

    @NotNull(message = "Provide Team A id")
    private Long teamAId;

    @NotNull(message = "Provide Team b id")
    private Long teamBId;

    private int totalOvers;

    private long startTime;
    private long endTime;

    private String groundUmpire1;
    private String groundUmpire2;
    private String thirdUmpire;

    private Long venueId;

}