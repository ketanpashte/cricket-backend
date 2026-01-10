package com.fastx.live_score.modules.match.dto;

import com.fastx.live_score.modules.team.ListTeamRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class Match {
    private Long id;
    private ListTeamRes teamA;
    private ListTeamRes teamB;
    private int tossWinner;
    private String electedTo;
    private int totalOvers;
    private String venue;
    private Long venueId;
    private String hostingNation;
    private MatchStatus matchStatus;
    private int winningTeam;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long tournamentId;
    private String tournamentName;
    private UUID liveMatchId;
    private String groundUmpire1;
    private String groundUmpire2;
    private String thirdUmpire;

}