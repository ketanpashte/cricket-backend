package com.fastx.live_score.mapper;

import com.fastx.live_score.modules.team.ListTeamRes;
import com.fastx.live_score.modules.tournament.dto.Tournament;
import com.fastx.live_score.modules.tournament.db.TournamentEntity;
import com.fastx.live_score.modules.tournament.db.TournamentGroupEntity;

import java.util.stream.Collectors;

public class TournamentMapper {

        public static Tournament mapToTournament(TournamentEntity entity) {
                Tournament.TournamentBuilder builder = Tournament.builder();

                builder.id(entity.getId());
                builder.name(entity.getName());
                builder.startDate(entity.getStartDate());
                builder.description(entity.getDescription());
                builder.endDate(entity.getEndDate());
                builder.location(entity.getLocation());
                builder.tournamentStatus(entity.getTournamentStatus());
                builder.type(entity.getTournamentType());
                builder.seriesFormat(entity.getSeriesFormat());
                builder.logoUrl(entity.getLogoUrl());
                builder.hostingNations(entity.getHostingNation());

                builder.maxParticipants(entity.getMaxParticipants());
                builder.tags(entity.getTags());
                builder.isGroup(entity.isGroup());

                builder.participatingTeams(
                                entity.getParticipatingTeams().stream()
                                                .map(teamEntity -> ListTeamRes.from(TeamMapper.toResponse(teamEntity)))
                                                .toList());

                builder.winner(TeamMapper.toResponse(entity.getWinner()));

                builder.matches(entity.getMatches().stream()
                                .map(MatchMapper::toMatch)
                                .toList());

                if (entity.isGroup() && entity.getGroups() != null && !entity.getGroups().isEmpty()) {
                        builder.groups(
                                        entity.getGroups().stream()
                                                        .map(TournamentMapper::mapToGroup)
                                                        .collect(Collectors.toList()));
                }

                builder.highlightMostRunsPlayer(entity.getHighlightMostRunsPlayer());
                builder.highlightMostRunsValue(entity.getHighlightMostRunsValue());
                builder.highlightMostWicketsPlayer(entity.getHighlightMostWicketsPlayer());
                builder.highlightMostWicketsValue(entity.getHighlightMostWicketsValue());
                builder.highlightBestFigurePlayer(entity.getHighlightBestFigurePlayer());
                builder.highlightBestFigureValue(entity.getHighlightBestFigureValue());

                return builder.build();
        }

        private static Tournament.Group mapToGroup(TournamentGroupEntity groupEntity) {
                return Tournament.Group.builder()
                                .id(groupEntity.getId())
                                .name(groupEntity.getName())
                                .teams(
                                                groupEntity.getTeams().stream()
                                                                .map(team -> ListTeamRes
                                                                                .from(TeamMapper.toResponse(team)))
                                                                .toList())
                                .build();
        }
}
