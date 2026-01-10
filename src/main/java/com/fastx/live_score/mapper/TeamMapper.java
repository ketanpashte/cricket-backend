package com.fastx.live_score.mapper;

import com.fastx.live_score.modules.player.dto.ListPlayerRes;
import com.fastx.live_score.modules.team.Team;
import com.fastx.live_score.modules.team.TeamEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TeamMapper {
    public static Team toResponse(TeamEntity entity) {
        if (entity == null) return null;
        Team.TeamBuilder builder = Team.builder()
                .id(entity.getId())
                .name(entity.getName())
                .shortCode(entity.getShortCode())
                .logoUrl(entity.getLogoUrl())
                .coach(entity.getCoach());

        if (entity.getCaptain() != null) {
            builder.captainId(entity.getCaptain().getId());
            builder.captainName(entity.getCaptain().getShortName());
        }
        if (entity.getViceCaptain() != null) {
            builder.viceCaptainId(entity.getViceCaptain().getId());
        }
        builder.bgImage(entity.getBgImage());
        builder.colorCode(entity.getColorCode());
        if (entity.getPlayers() != null) {
            List<ListPlayerRes> players = entity.getPlayers().stream()
                    .map(PlayerMapper::toPlayer)
                    .map(ListPlayerRes::toShortPlayer)
                    .collect(Collectors.toList());
            builder.players(players);
        }

        return builder.build();
    }
}
