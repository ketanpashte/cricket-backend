package com.fastx.live_score.modules.team;

import lombok.Getter;

@Getter
public class ListTeamRes {
    private final Long teamId;
    private final String teamName;
    private final String logUrl;
    private final String colorCode;
    private final String bgImageUrl;

    private ListTeamRes(Long teamId, String teamName, String logUrl, String colorCode, String bgImageUrl) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.logUrl = logUrl;
        this.colorCode = colorCode;
        this.bgImageUrl = bgImageUrl;
    }

    public static ListTeamRes from(Team team) {
        return new ListTeamRes(team.getId(),
                team.getName(),
                team.getLogoUrl(),
                team.getColorCode(),
                team.getBgImage());
    }
}
