package com.fastx.live_score.mapper;

import com.fastx.live_score.modules.player.dto.Player;
import com.fastx.live_score.modules.player.entity.PlayerEntity;

public class PlayerMapper {

    public static Player toPlayer(PlayerEntity entity) {
        if (entity == null) return null;

        return Player.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .shortName(entity.getShortName())
                .nationality(entity.getNationality())
                .role(entity.getRole())
                .battingStyle(entity.getBattingStyle())
                .bowlingStyle(entity.getBowlingStyle())
                .totalMatches(entity.getTotalMatches())
                .totalRuns(entity.getTotalRuns())
                .totalWickets(entity.getTotalWickets())
                .isActive(entity.isActive())
                .build();
    }


}
