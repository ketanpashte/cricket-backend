package com.fastx.live_score.modules.liveMatch.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class MatchDto {

    private String matchId;
    private String header;
    private String matchType;
    private String date;
    private String result;
    private String state;

    private Long teamAId;
    private String teamA;

    private Long teamBId;
    private String teamB;

    private List<PlayerInfo> teamAPlayers;
    private List<PlayerInfo> teamBPlayers;

    private List<InningsDto> innings;

    @Data
    @AllArgsConstructor
    public static class PlayerInfo {
        private Long id;
        private String name;
        private String role;
        @JsonProperty("isCaptain")
        private boolean isCaptain;
        @JsonProperty("isViceCaptain")
        private boolean isViceCaptain;
        @JsonProperty("isWicketKeeper")
        private boolean isWicketKeeper;
    }

    @Data
    @NoArgsConstructor
    public static class InningsDto {
        private String title;

        // Bowling team with ID + Name
        private List<PlayerInfo> bowlingTeam;

        private List<BatterDto> batters;
        private String extras;
        private int extrasTotal;
        private String total;

        private List<PlayerInfo> yetToBat;
        private List<FallOfWicketDTO> fallOfWickets;
        private List<BowlerDto> bowlers;
        List<PartnershipDto> partnerships;

        private String state;
        private String currentScore;

        private boolean isOverCompleted;

        private Long currentStrikerId;
        private String currentStriker;

        private Long currentNonStrikerId;
        private String currentNonStriker;

        private OverDto currentOver;
    }

    @Data
    @NoArgsConstructor
    public static class BatterDto {
        private Long id;
        private String name;
        private String dismissal;
        private int runs;
        private String minutes;
        private int balls;
        private int fours;
        private int sixes;
        private String strikeRate;
        private String summary;
    }

    @Data
    @NoArgsConstructor
    public static class BowlerDto {
        private Long id;
        private String name;
        private String overs;
        private int maidens;
        private int runsConceded;
        private int wickets;
        private String economy;
        private int dotBalls;
        private int fours;
        private int sixes;
        private int wides;
        private int noBalls;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartnershipDto {
        private int wicketNumber;
        private Instant startTime;
        private Instant endTime;
        private int totalRuns;
        private int totalBalls;
        private boolean brokenByWicket;
        private String state;
        private List<BatterContributionDto> batters;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatterContributionDto {
        private MatchDto.PlayerInfo player;
        private int runs;
        private int balls;
        private String strikeRate; // formatted like "136.4"
    }

    @Data
    @NoArgsConstructor
    public static class OverDto {
        private int overNumber;
        private List<String> balls;
        private Long bowlerId;
        private String bowlerName;
        private String summary;
        private String runs;
        private String teamRuns;
        private boolean completed;
    }
}
