package com.fastx.live_score.modules.tournament.dto;

import com.fastx.live_score.modules.team.ListTeamRes;
import com.fastx.live_score.modules.match.dto.Match;
import com.fastx.live_score.modules.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TournamentStatus tournamentStatus;
    private String type;
    private String seriesFormat;
    private String location;
    private String logoUrl;
    private List<String> hostingNations;

    private Integer maxParticipants;
    private List<String> tags;

    private List<Match> matches;
    private List<ListTeamRes> participatingTeams;

    private Team winner;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** ✅ New fields for grouping **/
    private boolean isGroup;
    private List<Group> groups;

    /** Highlights **/
    private String highlightMostRunsPlayer;
    private String highlightMostRunsValue;
    private String highlightMostWicketsPlayer;
    private String highlightMostWicketsValue;
    private String highlightBestFigurePlayer;
    private String highlightBestFigureValue;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Group {
        private Long id;
        private String name;
        private List<ListTeamRes> teams;
    }
}
