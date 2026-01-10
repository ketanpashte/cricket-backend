package com.fastx.live_score.modules.match.dto;

import lombok.Getter;

@Getter
public class ListMatchRes {
    private final Long matchId;
    private final String teamA;
    private final String teamB;

    private ListMatchRes(Long matchId, String teamA, String teamB) {
        this.matchId = matchId;
        this.teamA = teamA;
        this.teamB = teamB;
    }


    public static ListMatchRes from(Match match) {
        return new ListMatchRes(match.getId(),
                match.getTeamA().getTeamName(),
                match.getTeamB().getTeamName());
    }

}
