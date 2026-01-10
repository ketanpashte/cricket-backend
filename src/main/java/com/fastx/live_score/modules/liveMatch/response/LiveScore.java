package com.fastx.live_score.modules.liveMatch.response;

import lombok.Builder;
import lombok.Getter;
import org.example.*;
import io.micrometer.common.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class LiveScore {

    @Nullable
    private String message;
    private List<InningScore> allInnings;

    @Getter
    @Builder
    public static class InningScore {
        private String battingTeam;
        private int runs;
        private int wickets;
        private String over;
        private String striker;
        private String nonStriker;
        private String bowler;
        private String status;

        private String crr;
        private String rrr;
        private Integer target;
    }

    public static LiveScore fromMatchControl(MatchControl matchControl) {
        Match match = matchControl.match();
        Innings currentInnings = match.currentInnings();

        LiveScore.LiveScoreBuilder builder = LiveScore.builder()
                .allInnings(getAllInningsScore(match));

        if (currentInnings != null) {
            builder.message(chaseStatusString(currentInnings));
        } else {
            MatchResult matchResult = match.calculateResult();
            builder.message(matchResult.toString());
        }

        return builder.build();
    }

    private static @Nullable String chaseStatusString(Innings innings) {
        Integer target = innings.target();
        if (target == null) return null;

        int runsRequired = target - innings.score().teamRuns();
        Integer ballsRemaining = innings.numberOfBallsRemaining();
        if (ballsRemaining == null) return null;

        if (runsRequired <= 0) {
            return innings.battingTeam().teamName() + " has won the match!";
        }

        return innings.battingTeam().teamName() + " need " + runsRequired + " run" + (runsRequired == 1 ? "" : "s")
                + " in " + ballsRemaining + " ball" + (ballsRemaining == 1 ? "" : "s") + ".";
    }

    private static List<InningScore> getAllInningsScore(Match match) {
        List<InningScore> list = new ArrayList<>();
        for (Innings innings : match.inningsList()) {
            if (innings == null) continue;

            InningScore.InningScoreBuilder scoreBuilder = InningScore.builder()
                    .battingTeam(innings.battingTeam().teamName())
                    .runs(innings.score().teamRuns())
                    .wickets(innings.score().wickets())
                    .over(innings.overDotBallString())
                    .striker(innings.currentStriker() != null ? innings.currentStriker().player().name() : "")
                    .nonStriker(innings.currentNonStriker() != null ? innings.currentNonStriker().player().name() : "")
                    .bowler(innings.currentBowler() != null ? innings.currentBowler().bowler().name() : "")
                    .crr(innings.score().runsPerOver().toString())
                    .target(innings.target())
                    .status(innings.state().toString());

            if (innings.requiredRunRate() != null) {
                scoreBuilder.rrr(innings.requiredRunRate().toString());
            }
            list.add(scoreBuilder
                    .build());
        }
        return list;
    }
}
