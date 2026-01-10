package com.fastx.live_score.modules.liveMatch.response;

import org.example.*;
import org.example.events.BatterInningsCompletedEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.Crictils.pluralize;

public class MatchDtoMapper {

    // ===========================
    // Main Mapper for MatchDto
    // ===========================
    public static MatchDto toDto(MatchControl control) {
        Match match = control.match();
        MatchDto dto = new MatchDto();

        // Teams Info
        SimpleLineUp lineUpA = (SimpleLineUp) match.teams().get(0);
        SimpleLineUp lineUpB = (SimpleLineUp) match.teams().get(1);

        dto.setTeamAId(lineUpA.getId());
        dto.setTeamA(lineUpA.teamName());
        dto.setTeamBId(lineUpB.getId());
        dto.setTeamB(lineUpB.teamName());

        dto.setTeamAPlayers(lineUpA.battingOrder().stream().map(p -> mapPlayer(p, lineUpA)).toList());
        dto.setTeamBPlayers(lineUpB.battingOrder().stream().map(p -> mapPlayer(p, lineUpB)).toList());

        // Match Header & Type
        dto.setHeader(match.teams().stream()
                .map(LineUp::teamName)
                .map(String::toUpperCase)
                .collect(Collectors.joining(" vs ")));
        dto.setMatchId(match.id().toString());
        dto.setMatchType(getMatchType(match));
        dto.setState(match.state().name());
        dto.setDate(formatMatchDate(match));
        dto.setResult(match.result() != null ? match.result().toString() : null);

        // Innings Info
        List<MatchDto.InningsDto> inningsDtos = match.inningsList()
                .stream()
                .map(inning -> mapInnings(inning, control))
                .collect(Collectors.toList());
        Collections.reverse(inningsDtos);
        dto.setInnings(inningsDtos);

        return dto;
    }

    public static MatchDto.PlayerInfo mapPlayer(Player player) {
        return mapPlayer(player, null);
    }

    public static MatchDto.PlayerInfo mapPlayer(Player player, SimpleLineUp lineUp) {
        MatchDto.PlayerInfo info = new MatchDto.PlayerInfo(player.id(), player.scorecardName(), null, false, false,
                false);
        if (player instanceof SimplePlayer sp) {
            info.setRole(sp.role());
        }

        if (lineUp != null) {
            if (lineUp.captain() != null && player.id().equals(lineUp.captain().id())) {
                info.setCaptain(true);
            }
            if (lineUp.viceCaptain() != null && player.id().equals(lineUp.viceCaptain().id())) {
                info.setViceCaptain(true);
            }
            if (lineUp.wicketKeeper() != null && player.id().equals(lineUp.wicketKeeper().id())) {
                info.setWicketKeeper(true);
            }
        }
        return info;
    }

    public static MatchDto.InningsDto mapInnings(Innings innings, MatchControl control) {
        MatchDto.InningsDto dto = new MatchDto.InningsDto();
        dto.setTitle(buildInningsTitle(innings, control));
        SimpleLineUp lineUp = (SimpleLineUp) innings.bowlingTeam();
        dto.setBowlingTeam(lineUp.battingOrder().stream().map(
                MatchDtoMapper::mapPlayer).toList());
        dto.setState(innings.state().name());
        dto.setCurrentScore(getCurrentScore(innings));

        if (innings.currentOver() != null) {
            dto.setOverCompleted(innings.currentOver().isComplete());
        }
        if (innings.currentStriker() != null) {
            dto.setCurrentStrikerId(innings.currentStriker().player().id());
            dto.setCurrentStriker(innings.currentStriker().player().name());
        }
        if (innings.currentNonStriker() != null) {
            dto.setCurrentNonStrikerId(innings.currentNonStriker().player().id());
            dto.setCurrentNonStriker(innings.currentNonStriker().player().name());
        }

        // Batters
        dto.setBatters(innings.batterInningsList().stream()
                .map(bi -> mapBatterDto(bi, innings))
                .collect(Collectors.toList()));

        // Extras
        dto.setExtras(getExtraDetails(innings.score()));
        dto.setExtrasTotal(innings.score().extras());
        dto.setTotal("TOTAL (" + innings.score().wickets() + " wkts; " +
                innings.overDotBallString() + " overs) " + innings.score().teamRuns());

        dto.setYetToBat(innings.yetToBat().stream().map(
                MatchDtoMapper::mapPlayer).toList());

        // Fall of Wickets
        dto.setFallOfWickets(getFallOfWickets(innings, control));

        // Bowlers
        dto.setBowlers(innings.bowlerInningsList().stream()
                .map(MatchDtoMapper::mapBowlerDto)
                .collect(Collectors.toList()));
        // partnerships
        dto.setPartnerships(innings.partnerships().stream()
                .map(MatchDtoMapper::mapPartnership)
                .collect(Collectors.toList()));

        dto.setCurrentOver(mapOverDto(innings.currentOver(), innings));

        return dto;
    }

    // ===========================
    // Batter Mapper
    // ===========================
    public static MatchDto.BatterDto mapBatterDto(BatterInnings bi, Innings innings) {
        MatchDto.BatterDto batter = new MatchDto.BatterDto();
        batter.setId(bi.player().id());
        batter.setName(getBatterName(bi.player(), innings));
        batter.setSummary(bi.toString());
        batter.setDismissal(bi.dismissal() != null ? bi.dismissal().toScorecardString(innings.bowlingTeam())
                : (bi.state() == BattingState.RETIRED || bi.state() == BattingState.RETIRED_OUT ? "retired"
                        : "not out"));
        Score s = bi.score();
        batter.setRuns(s.batterRuns());
        batter.setBalls(s.validDeliveries());
        batter.setFours(s.fours());
        batter.setSixes(s.sixes());
        batter.setStrikeRate(s.battingStrikeRate() != null ? String.format("%.1f", s.battingStrikeRate()) : "-");
        return batter;
    }

    public static MatchDto.PartnershipDto mapPartnership(Partnership partnership) {
        Score score = partnership.score();

        // first batter stats
        Score firstScore = partnership.firstBatterContribution().score();
        MatchDto.BatterContributionDto first = MatchDto.BatterContributionDto.builder()
                .player(mapPlayer(partnership.firstBatter()))
                .runs(firstScore.batterRuns())
                .balls(firstScore.validDeliveries())
                .strikeRate(firstScore.battingStrikeRate() != null
                        ? String.format("%.1f", firstScore.battingStrikeRate())
                        : "-")
                .build();

        // second batter stats
        Score secondScore = partnership.secondBatterContribution().score();
        MatchDto.BatterContributionDto second = MatchDto.BatterContributionDto.builder()
                .player(mapPlayer(partnership.secondBatter()))
                .runs(secondScore.batterRuns())
                .balls(secondScore.validDeliveries())
                .strikeRate(secondScore.battingStrikeRate() != null
                        ? String.format("%.1f", secondScore.battingStrikeRate())
                        : "-")
                .build();

        return MatchDto.PartnershipDto.builder()
                .wicketNumber(partnership.wicketNumber())
                .startTime(partnership.startTime())
                .endTime(partnership.endTime())
                .totalRuns(score.teamRuns())
                .totalBalls(score.validDeliveries())
                .brokenByWicket(partnership.brokenByWicket())
                .state(partnership.state().name())
                .batters(List.of(first, second))
                .build();
    }

    // ===========================
    // Bowler Mapper
    // ===========================
    public static MatchDto.BowlerDto mapBowlerDto(BowlerInnings bi) {
        MatchDto.BowlerDto bowler = new MatchDto.BowlerDto();
        bowler.setId(bi.bowler().id());
        bowler.setName(bi.bowler().scorecardName());
        Score s = bi.score();
        bowler.setOvers(bi.overDotBallString());
        bowler.setMaidens(bi.maidens());
        bowler.setRunsConceded(s.bowlerRuns());
        bowler.setWickets(bi.wickets());
        bowler.setEconomy(
                String.format("%d.%d", s.bowlerEconomyRate().nearestIntValue(), s.bowlerEconomyRate().firstDecimal()));
        bowler.setDotBalls(s.dots());
        bowler.setFours(s.fours());
        bowler.setSixes(s.sixes());
        bowler.setWides(s.wideDeliveries());
        bowler.setNoBalls(s.noBalls());
        return bowler;
    }

    // ===========================
    // Over Mapper
    // ===========================
    public static MatchDto.OverDto mapOverDto(Over over, Innings innings) {
        if (over == null)
            return null;
        MatchDto.OverDto overDto = new MatchDto.OverDto();
        overDto.setOverNumber(over.overNumber() + 1);
        overDto.setCompleted(over.isComplete());
        Player bowler = over.bowler();
        overDto.setBowlerId(bowler.id());
        overDto.setBowlerName(bowler.scorecardName());
        overDto.setBalls(over.balls().list().stream()
                .map(ball -> Score.toText(ball.score()))
                .collect(Collectors.toList()));
        Player striker = over.striker();
        Player nonStriker = over.nonStriker();
        overDto.setRuns(String.valueOf(over.score().teamRuns()));
        overDto.setTeamRuns(innings.score().teamRuns() + "/" + innings.score().wickets());
        overDto.setSummary(
                bowler.scorecardName() + " to " + striker.scorecardName() + " & " + nonStriker.scorecardName());
        return overDto;
    }

    // ===========================
    // Utility Methods
    // ===========================
    private static String getMatchType(Match match) {
        return match.oversPerInnings() == null
                ? match.numberOfScheduledDays() + " day match"
                : match.oversPerInnings() == 20 ? "T20" : "One day match";
    }

    private static String formatMatchDate(Match match) {
        if (match.scheduledStartTime() == null || match.timeZone() == null)
            return null;
        LocalDateTime startTime = LocalDateTime.ofInstant(match.scheduledStartTime(), match.timeZone().toZoneId());
        if (match.numberOfScheduledDays() == 1) {
            return startTime.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        } else {
            LocalDateTime endTime = startTime.plusDays(match.numberOfScheduledDays() - 1);
            String endFormat = startTime.getMonthValue() == endTime.getMonthValue() ? "d yyyy" : "MMM d yyyy";
            return startTime.format(DateTimeFormatter.ofPattern("MMM d")) + " to " +
                    endTime.format(DateTimeFormatter.ofPattern(endFormat));
        }
    }

    private static String buildInningsTitle(Innings innings, MatchControl control) {
        boolean multipleInnings = control.match().numberOfInningsPerTeam() > 1;
        String inningsNum = multipleInnings ? " " + Crictils.withOrdinal(innings.inningsNumberForBattingTeam()) : "";
        String title = innings.battingTeam().teamName() + inningsNum + " Innings";
        if (innings.originalMaxOvers() != null) {
            title += " (" + pluralize(innings.originalMaxOvers(), "over") + " maximum)";
        }
        if (multipleInnings && innings.target() != null) {
            title += " (target: " + pluralize(innings.target(), "run") + ")";
        }
        return title;
    }

    private static String getCurrentScore(Innings innings) {
        return innings.score().teamRuns() + "/" + innings.score().wickets() +
                " in " + innings.overDotBallString() + " overs";
    }

    public static String getLastWicket(Innings innings, MatchControl control) {
        return control.history().stream()
                .filter(MatchControl.sameInnings(innings))
                .filter(me -> me.event() instanceof BatterInningsCompletedEvent)
                .filter(me -> ((BatterInningsCompletedEvent) me.event()).reason() != BattingState.INNINGS_ENDED)
                .reduce((first, second) -> second)
                .map(me -> {
                    BatterInningsCompletedEvent ev = (BatterInningsCompletedEvent) me.event();
                    BatterInnings batterInnings = innings.batterInnings(ev.batter());
                    Score s = batterInnings.score();
                    return ev.batter().name() + " " + s.batterRuns() + "(" + s.validDeliveries() + ")";
                })
                .orElse(null);
    }

    public static List<FallOfWicketDTO> getFallOfWickets(Innings innings, MatchControl control) {
        return control.history().stream()
                .filter(MatchControl.sameInnings(innings))
                .filter(me -> me.event() instanceof BatterInningsCompletedEvent)
                .filter(me -> ((BatterInningsCompletedEvent) me.event()).reason() != BattingState.INNINGS_ENDED)
                .map(me -> {
                    Innings current = Objects.requireNonNull(me.match().currentInnings());
                    Score score = current.score();
                    BatterInningsCompletedEvent ev = (BatterInningsCompletedEvent) me.event();

                    int wickets = score.wickets();
                    int runs = score.teamRuns();
                    String batterName = ev.batter().name();
                    String overOrReason = (ev.reason() == BattingState.RETIRED_OUT
                            || ev.reason() == BattingState.RETIRED)
                                    ? "retired"
                                    : current.overDotBallString() + " ov";

                    return new FallOfWicketDTO(wickets, runs, batterName, overOrReason);
                })
                .collect(Collectors.toList());
    }

    public static String getBatterName(Player bi, Innings innings) {
        String name = bi.scorecardName();
        LineUp battingTeam = innings.battingTeam();
        if (bi.samePlayer(battingTeam.wicketKeeper()))
            name += " (wk)";
        if (bi.samePlayer(battingTeam.captain()))
            name += " (c)";
        if (battingTeam instanceof SimpleLineUp sl && bi.samePlayer(sl.viceCaptain()))
            name += " (vc)";
        if (innings.currentStriker() != null && innings.currentStriker().player().samePlayer(bi))
            name += " *";
        return name;
    }

    private static String getExtraDetails(Score score) {
        if (score.extras() == 0)
            return "";
        StringBuilder sb = new StringBuilder("(");
        String sep = ", ";
        if (score.byes() > 0)
            sb.append("b ").append(score.byes()).append(sep);
        if (score.legByes() > 0)
            sb.append("lb ").append(score.legByes()).append(sep);
        if (score.wides() > 0)
            sb.append("w ").append(score.wides()).append(sep);
        if (score.noBalls() > 0)
            sb.append("nb ").append(score.noBalls()).append(sep);
        if (score.penaltyRuns() > 0)
            sb.append("p ").append(score.penaltyRuns()).append(sep);
        return sb.substring(0, sb.length() - sep.length()) + ")";
    }
}
