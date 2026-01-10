package com.fastx.live_score.modules.tournament.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRequest {

    @NotNull(message = "Provide a name for the tournament")
    private String name;

    @NotNull(message = "Provide a description for the tournament")
    private String description;

    @NotNull(message = "Provide a start date for the tournament")
    private long startDate;

    @NotNull(message = "Provide an end date for the tournament")
    private long endDate;

    @NotNull(message = "Tournament status has to be set")
    private TournamentStatus tournamentStatus;

    @NotNull(message = "Hosting Nations are required")
    private List<String> hostingNations;

    @NotNull(message = "Tournament type has to be set")
    private String type;

    private String seriesFormat;

    private String location;

    private String logoUrl;

    private List<Long> participatingTeamIds;

    @NotNull(message = "Max participants is required")
    private Integer maxParticipants;

    private List<String> tags;

    private Boolean isGroup = false; // default false

    private List<GroupRequest> groups;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupRequest {
        @NotNull(message = "Group name cannot be null")
        private String name;

        @NotNull(message = "Teams for group cannot be null")
        private List<Long> teamIds;
    }
}
