package com.fastx.live_score.modules.liveMatch.response;

import io.micrometer.common.lang.Nullable;
import lombok.Data;
import org.example.*;

import java.util.List;

import static com.fastx.live_score.modules.liveMatch.response.MatchDtoMapper.getBatterName;

@Data
public class LiveLineScore {

    private String crr;
    private String rrr;
    private Integer target;
    private String requiredMessage;

    BatterDto currentStriker;
    BatterDto currentNonStriker;
    BowlerDto currentBowler;
    List<String> yetToBat;
    String lastWicket;
    String partnership;


    public static LiveLineScore getFromMatch(MatchControl control) {
        LiveLineScore score = new LiveLineScore();
        Match match = control.match();
        Innings innings = match.currentInnings();
        if (innings == null) return null;
        score.setRequiredMessage(chaseStatusString(innings));
        BatterInnings currentStriker = innings.currentStriker();
        if (currentStriker != null) {
            score.setCurrentStriker(BatterDto.mapBatterDto(currentStriker, innings));
        }
        if (innings.requiredRunRate() != null) {
            score.setRrr(innings.requiredRunRate().toString());
        }
        score.setCrr(innings.score().runsPerOver().toString());
        score.setTarget(innings.target());
        BatterInnings currentNonStriker = innings.currentNonStriker();
        if (currentNonStriker != null) {
            score.setCurrentNonStriker(BatterDto.mapBatterDto(currentNonStriker, innings));
        }
        BowlerInnings currenBowler = innings.currentBowler();
        if (currenBowler != null) {
            score.setCurrentBowler(BowlerDto.mapBowlerDto(currenBowler));
        }
        score.setYetToBat(innings.yetToBat().stream().map(Player::name).toList());
        score.setLastWicket(MatchDtoMapper.getLastWicket(innings, control));
        Partnership partnership = innings.currentPartnership();
        if (partnership != null) {
            Score partnershipScore = partnership.score();
            score.setPartnership(partnershipScore.teamRuns() + "(" + partnershipScore.validDeliveries() + ")");
        }
        return score;
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

    @Data
    public static class BatterDto {
        private String name;
        private int runs;
        private int balls;
        private int fours;
        private int sixes;
        private String strikeRate;

        public static BatterDto mapBatterDto(BatterInnings bi, Innings innings) {
            BatterDto batter = new BatterDto();
            batter.setName(getBatterName(bi.player(), innings));
            Score s = bi.score();
            batter.setRuns(s.batterRuns());
            batter.setBalls(s.validDeliveries());
            batter.setFours(s.fours());
            batter.setSixes(s.sixes());
            batter.setStrikeRate(s.battingStrikeRate() != null ? String.format("%.1f", s.battingStrikeRate()) : "-");
            return batter;
        }

    }

    @Data
    public static class BowlerDto {
        private String name;
        private String overs;
        private int runsConceded;
        private int wickets;
        private String economy;

        public static BowlerDto mapBowlerDto(BowlerInnings bi) {
            BowlerDto bowler = new BowlerDto();
            bowler.setName(bi.bowler().scorecardName());
            Score s = bi.score();
            bowler.setOvers(bi.overDotBallString());
            bowler.setRunsConceded(s.bowlerRuns());
            bowler.setWickets(bi.wickets());
            bowler.setEconomy(String.format("%d.%d", s.bowlerEconomyRate().nearestIntValue(), s.bowlerEconomyRate().firstDecimal()));
            return bowler;
        }

    }

    @Data
    public static class InningScoreDto {
        private int runs;
        private int wickets;
        private double overs;
    }
}
