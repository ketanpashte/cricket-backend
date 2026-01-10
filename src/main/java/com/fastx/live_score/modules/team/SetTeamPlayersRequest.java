package com.fastx.live_score.modules.team;

import lombok.Data;

import java.util.List;

@Data
public class SetTeamPlayersRequest {
    private List<Long> playerIds;
    private Long captainId;
    private Long viceCaptainId;
}
