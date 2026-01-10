package com.fastx.live_score.modules.liveMatch.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.*;

import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OverEndingSummary {

    // Batters (explicit numbers instead of a single "summary" string)
    private String strikerName;
    private Integer strikerRuns;
    private Integer strikerBalls;

    private String nonStrikerName;
    private Integer nonStrikerRuns;
    private Integer nonStrikerBalls;

    // Bowler
    private String bowlerName;
    private String bowlerOvers;   // keep as string like "4.0" or "3.2"
    private Integer bowlerRuns;
    private Integer bowlerMaidens;
    private Integer bowlerWickets;

    private int teamScore;
    private int teamWickets;
    private String overSummary;
    private int overNumber;

    public static OverEndingSummary fromMatch(Match match) {
        Innings innings = match.currentInnings();
        if (innings == null) {
            return null;
        }

        OverEndingSummary summary = new OverEndingSummary();
        Over over = innings.currentOver();
        if (over != null) {
            String collect = over.balls().list().stream()
                    .map(ball -> Score.toText(ball.score())).collect(Collectors.joining());
            summary.setOverSummary(collect);
            summary.setOverNumber(over.overNumber());
        }
        if (innings.currentBowler() != null) {
            var cb = innings.currentBowler();
            summary.setBowlerName(cb.bowler().name());
            summary.setBowlerOvers(cb.overDotBallString()); // e.g. "4.0" or "3.2"
            try {
                summary.setBowlerRuns(cb.score().bowlerRuns());
            } catch (Exception e) {
                // fallback if method name differs; set null or 0 as appropriate
                summary.setBowlerRuns(null);
            }
            try {
                summary.setBowlerMaidens(cb.maidens());
            } catch (Exception e) {
                summary.setBowlerMaidens(null);
            }
            try {
                // some models use `wickets()` or `bowlerWickets()` - adapt if needed
                summary.setBowlerWickets(cb.wickets());
            } catch (Exception e) {
                try {
                    summary.setBowlerWickets(cb.score().wickets());
                } catch (Exception ex) {
                    summary.setBowlerWickets(null);
                }
            }
        }

        // Striker
        if (innings.currentStriker() != null) {
            BatterInnings striker = innings.currentStriker();
            summary.setStrikerRuns(striker.runs());
            summary.setStrikerName(striker.player().scorecardName());
            // validDeliveries() used previously — keep it for balls faced
            try {
                summary.setStrikerBalls(striker.score().validDeliveries());
            } catch (Exception e) {
                summary.setStrikerBalls(null);
            }
        }

        // Non-striker
        if (innings.currentNonStriker() != null) {
            BatterInnings nonStriker = innings.currentNonStriker();
            summary.setNonStrikerRuns(nonStriker.runs());
            summary.setNonStrikerName(nonStriker.player().scorecardName());
            try {
                summary.setNonStrikerBalls(nonStriker.score().validDeliveries());
            } catch (Exception e) {
                summary.setNonStrikerBalls(null);
            }
        }


        summary.setTeamScore(innings.score().teamRuns());
        summary.setTeamWickets(innings.score().wickets());

        return summary;
    }
}
