package com.fastx.live_score.modules.team;

import com.fastx.live_score.modules.player.dto.ListPlayerRes;

import java.util.List;

public record TeamPlayerInfo(List<ListPlayerRes> players, Long teamId, Long captainId, Long viceCaptainId) {

}
