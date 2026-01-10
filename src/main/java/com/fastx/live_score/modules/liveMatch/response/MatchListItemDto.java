package com.fastx.live_score.modules.liveMatch.response;

import com.fastx.live_score.modules.match.db.MatchEntity;
import com.fastx.live_score.modules.tournament.db.TournamentEntity;
import org.example.Match;
import org.example.MatchControl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchListItemDto {

    private Long matchId;
    private String liveMatchId;
    private String teamA;
    private String teamALogo;
    private String teamB;
    private String teamBLogo;
    private String venue;
    private String tossMessage;
    private String tournamentName;
    private String tournamentLogo;
    private String status;
    private LiveScore liveScore;

    public static MatchListItemDto fromMatchController(MatchEntity matchEntity, MatchControl control) {
        if (matchEntity == null) return null;

        MatchListItemDtoBuilder builder = MatchListItemDto.builder()
                .matchId(matchEntity.getId())
                .liveMatchId(matchEntity.getLiveMatchId() != null ? matchEntity.getLiveMatchId().toString() : null)
                .teamA(getTeamName(matchEntity.getTeamEntityA(), "Team A"))
                .teamB(getTeamName(matchEntity.getTeamEntityB(), "Team B"))
                .teamALogo(matchEntity.getTeamEntityA() != null ? matchEntity.getTeamEntityA().getLogoUrl() : null)
                .teamBLogo(matchEntity.getTeamEntityB() != null ? matchEntity.getTeamEntityB().getLogoUrl() : null)
                .status(matchEntity.getMatchStatus() != null ? matchEntity.getMatchStatus().name() : "Not Started");

        if (matchEntity.getVenue() != null) {
            builder.venue(matchEntity.getVenue().getName());
        }

        TournamentEntity tournament = matchEntity.getTournament();
        if (tournament != null) {
            builder.tournamentName(tournament.getName())
                    .tournamentLogo(tournament.getLogoUrl());
        }

        // Case 1: Match not started or control is null
        if (matchEntity.getLiveMatchId() == null || control == null) {
            builder.tossMessage("Match not started")
                    .liveScore(null);
            return builder.build();
        }

        // Case 2: Match started
        Match match = control.match();

        String teamA = !match.teams().isEmpty() ? match.teams().get(0).teamName() : builder.build().getTeamA();
        String teamB = match.teams().size() > 1 ? match.teams().get(1).teamName() : builder.build().getTeamB();

        String tossWinner = matchEntity.getTossWinner() == 1
                ? matchEntity.getTeamEntityA().getName()
                : matchEntity.getTeamEntityB().getName();
        String electedTo = matchEntity.getElectedTo();
        String tossMessage = String.format("%s won the toss and chose to %s first", tossWinner, electedTo);

        builder.teamA(teamA)
                .teamB(teamB)
                .tossMessage(tossMessage)
                .liveScore(LiveScore.fromMatchControl(control));

        return builder.build();
    }

    private static String getTeamName(Object teamEntity, String defaultName) {
        if (teamEntity == null) return defaultName;
        try {
            var method = teamEntity.getClass().getMethod("getName");
            return (String) method.invoke(teamEntity);
        } catch (Exception e) {
            return defaultName;
        }
    }
}
