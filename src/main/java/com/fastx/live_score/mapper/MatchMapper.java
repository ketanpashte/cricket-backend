package com.fastx.live_score.mapper;

import com.fastx.live_score.modules.team.ListTeamRes;
import com.fastx.live_score.modules.match.dto.Match;
import com.fastx.live_score.modules.match.db.MatchEntity;
import com.fastx.live_score.modules.venue.db.entity.Venue;

public class MatchMapper {
    public static Match toMatch(MatchEntity matchEntity) {
        Match.MatchBuilder builder = Match.builder();
        builder.id(matchEntity.getId());
        if (matchEntity.getTeamEntityA() != null) {
            builder.teamA(ListTeamRes.from(TeamMapper.toResponse(matchEntity.getTeamEntityA())));
        }
        if (matchEntity.getTeamEntityB() != null) {
            builder.teamB(ListTeamRes.from(TeamMapper.toResponse(matchEntity.getTeamEntityB())));
        }
        builder.startTime(matchEntity.getStartTime());
        Venue venue = matchEntity.getVenue();
        if (venue != null) {
            builder.venue(venue.getName());
            builder.venueId(venue.getId());
            builder.hostingNation(venue.getCountry());
        }
        builder.totalOvers(matchEntity.getTotalOvers());
        builder.endTime(matchEntity.getEndTime());
        builder.winningTeam(matchEntity.getWinningTeam());
        builder.electedTo(matchEntity.getElectedTo());
        builder.matchStatus(matchEntity.getMatchStatus());
        builder.tossWinner(matchEntity.getTossWinner());
        builder.tournamentId(matchEntity.getTournament().getId());
        builder.tournamentName(matchEntity.getTournament().getName());
        builder.groundUmpire1(matchEntity.getGroundUmpire1());
        builder.groundUmpire2(matchEntity.getGroundUmpire2());
        builder.thirdUmpire(matchEntity.getThirdUmpire());
        builder.liveMatchId(matchEntity.getLiveMatchId());
        return builder.build();
    }
}

