package com.fastx.live_score.modules.tournament.service;

import com.fastx.live_score.modules.team.TeamEntity;
import com.fastx.live_score.mapper.TournamentMapper;
import com.fastx.live_score.modules.match.db.MatchEntityRepository;
import com.fastx.live_score.modules.team.TeamRepository;
import com.fastx.live_score.modules.tournament.db.TournamentEntity;
import com.fastx.live_score.modules.tournament.db.TournamentGroupEntity;
import com.fastx.live_score.modules.tournament.db.TournamentJpaRepository;
import com.fastx.live_score.modules.tournament.dto.Tournament;
import com.fastx.live_score.modules.tournament.dto.TournamentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TournamentServiceImpl implements TournamentService {
    private final TournamentJpaRepository tournamentJpaRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public TournamentServiceImpl(MatchEntityRepository matchEntityRepository,
            TournamentJpaRepository tournamentJpaRepository, TeamRepository teamRepository) {
        this.tournamentJpaRepository = tournamentJpaRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Tournament createNewTournament(TournamentRequest request) {
        TournamentEntity formTournamentRequest = createFormTournamentRequest(request);
        TournamentEntity tournamentEntity = tournamentJpaRepository.save(formTournamentRequest);
        return TournamentMapper.mapToTournament(tournamentEntity);
    }

    @Override
    public Tournament updateTournament(Long tournamentId, TournamentRequest request) {
        TournamentEntity tournamentEntity = tournamentJpaRepository.findById(tournamentId).orElseThrow();
        TournamentEntity formTournamentRequest = createFormTournamentRequest(request, tournamentEntity);
        return TournamentMapper.mapToTournament(tournamentJpaRepository.save(formTournamentRequest));
    }

    @Override
    public Tournament updateHighlights(Long tournamentId,
            com.fastx.live_score.modules.tournament.dto.TournamentHighlightsRequest request) {
        TournamentEntity tournamentEntity = tournamentJpaRepository.findById(tournamentId).orElseThrow();

        Optional.ofNullable(request.getHighlightMostRunsPlayer())
                .ifPresent(tournamentEntity::setHighlightMostRunsPlayer);
        Optional.ofNullable(request.getHighlightMostRunsValue()).ifPresent(tournamentEntity::setHighlightMostRunsValue);
        Optional.ofNullable(request.getHighlightMostWicketsPlayer())
                .ifPresent(tournamentEntity::setHighlightMostWicketsPlayer);
        Optional.ofNullable(request.getHighlightMostWicketsValue())
                .ifPresent(tournamentEntity::setHighlightMostWicketsValue);
        Optional.ofNullable(request.getHighlightBestFigurePlayer())
                .ifPresent(tournamentEntity::setHighlightBestFigurePlayer);
        Optional.ofNullable(request.getHighlightBestFigureValue())
                .ifPresent(tournamentEntity::setHighlightBestFigureValue);

        return TournamentMapper.mapToTournament(tournamentJpaRepository.save(tournamentEntity));
    }

    @Override
    public Tournament getTournamentById(Long tournamentId) {
        TournamentEntity tournamentEntity = tournamentJpaRepository.findById(tournamentId).orElseThrow();
        return TournamentMapper.mapToTournament(tournamentEntity);
    }

    @Override
    public List<Tournament> getAllTournaments() {
        List<TournamentEntity> tournamentEntityList = tournamentJpaRepository.findAll();
        return tournamentEntityList.stream().map(TournamentMapper::mapToTournament).toList();
    }

    @Override
    public List<Tournament> searchTournament(String query) {
        List<TournamentEntity> tournamentEntityList;

        if (query == null || query.isEmpty()) {
            tournamentEntityList = tournamentJpaRepository.findAll();
        } else {
            tournamentEntityList = tournamentJpaRepository
                    .findByNameContainsIgnoreCase(query);
        }

        return tournamentEntityList.stream().map(TournamentMapper::mapToTournament).toList();
    }

    @Override
    public void deleteTournament(Long tournamentId) {
        tournamentJpaRepository.deleteById(tournamentId);
    }

    @Override
    public void assignWinner(Long tournamentId, Long teamId) {
        TeamEntity teamEntity = teamRepository.findById(teamId).orElseThrow();
        TournamentEntity tournament = tournamentJpaRepository.findById(tournamentId).orElseThrow();
        tournament.setWinner(teamEntity);
        tournamentJpaRepository.save(tournament);
    }

    private TournamentEntity createFormTournamentRequest(TournamentRequest request, TournamentEntity entity) {
        Optional.ofNullable(request.getName()).ifPresent(entity::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(entity::setDescription);
        Optional.ofNullable(request.getLocation()).ifPresent(entity::setLocation);
        Optional.ofNullable(request.getLogoUrl()).ifPresent(entity::setLogoUrl);

        // Start Date
        if (request.getStartDate() != 0) {
            entity.setStartDate(LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(request.getStartDate()),
                    ZoneId.systemDefault()));
        }

        // End Date
        if (request.getEndDate() != 0) {
            entity.setEndDate(LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(request.getEndDate()),
                    ZoneId.systemDefault()));
        }

        // Tournament Status
        if (request.getTournamentStatus() != null) {
            entity.setTournamentStatus(request.getTournamentStatus());
        }
        if (request.getHostingNations() != null) {
            entity.setHostingNation(request.getHostingNations());
        }

        // Tournament Type
        if (request.getType() != null) {
            entity.setTournamentType(request.getType());
        }

        if (request.getSeriesFormat() != null) {
            entity.setSeriesFormat(request.getSeriesFormat());
        }

        // ✅ Max Participants
        if (request.getMaxParticipants() != null && request.getMaxParticipants() > 0) {
            entity.setMaxParticipants(request.getMaxParticipants());
        }

        // ✅ Tags (replace old tags with new list)
        if (request.getTags() != null) {
            entity.setTags(new ArrayList<>(request.getTags()));
        }

        // Participating Teams (ensure uniqueness)
        if (request.getParticipatingTeamIds() != null) {
            Set<Long> uniqueTeamIds = new HashSet<>(request.getParticipatingTeamIds());
            entity.setParticipatingTeams(teamRepository.findAllById(uniqueTeamIds));
        }

        // ✅ Handle Grouping Logic
        if (request.getIsGroup() != null) {
            entity.setGroup(request.getIsGroup());

            if (request.getIsGroup()) {
                // ✅ Replace groups in-place (DO NOT replace reference)
                entity.getGroups().clear();

                if (request.getGroups() != null && !request.getGroups().isEmpty()) {
                    for (TournamentRequest.GroupRequest groupRequest : request.getGroups()) {
                        TournamentGroupEntity groupEntity = new TournamentGroupEntity();
                        groupEntity.setName(groupRequest.getName());
                        groupEntity.setTournament(entity); // Important for mapping

                        if (groupRequest.getTeamIds() != null && !groupRequest.getTeamIds().isEmpty()) {
                            List<TeamEntity> groupTeams = teamRepository.findAllById(groupRequest.getTeamIds());
                            groupEntity.setTeams(groupTeams);
                        }

                        entity.getGroups().add(groupEntity); // Add to existing list
                    }
                }
            } else {
                // ✅ If grouping is turned off, remove all groups
                entity.getGroups().clear();
            }
        }

        return entity;
    }

    private TournamentEntity createFormTournamentRequest(TournamentRequest request) {
        TournamentEntity entity = new TournamentEntity();
        return createFormTournamentRequest(request, entity);
    }

}
