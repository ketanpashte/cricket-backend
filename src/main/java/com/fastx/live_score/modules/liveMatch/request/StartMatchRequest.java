package com.fastx.live_score.modules.liveMatch.request;

import lombok.Data;

import java.util.List;

@Data
public class StartMatchRequest {
    private Long matchId;
    private List<Long> teamAPlayers;
    private List<Long> teamBPlayers;
    private String electedTo;
    private Long electedTeamId;
    private int overs;

    private Long teamACaptainId;
    private Long teamAViceCaptainId;
    private Long teamAWicketKeeperId;

    private Long teamBCaptainId;
    private Long teamBViceCaptainId;
    private Long teamBWicketKeeperId;
}
