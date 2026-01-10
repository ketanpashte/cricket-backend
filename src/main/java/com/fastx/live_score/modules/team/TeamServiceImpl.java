package com.fastx.live_score.modules.team;

import com.fastx.live_score.modules.player.dto.ListPlayerRes;
import com.fastx.live_score.modules.player.dto.Player;
import com.fastx.live_score.modules.player.entity.PlayerEntity;
import com.fastx.live_score.mapper.PlayerMapper;
import com.fastx.live_score.mapper.TeamMapper;
import com.fastx.live_score.modules.player.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository,
                           PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void saveTeams(List<TeamRequest> requestList) {
        List<TeamEntity> teams = requestList.stream()
                .peek(this::validate)
                .map(this::buildTeamEntity)
                .collect(Collectors.toList());
        teamRepository.saveAll(teams);
    }

    @Override
    public void updateTeam(Long teamId, TeamRequest request) {
        TeamEntity teamEntity = teamRepository.findById(teamId).orElseThrow();
        teamRepository.save(buildTeamEntity(teamEntity, request));
    }

    @Override
    public Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0) throw new IllegalArgumentException("Invalid team ID");

        return teamRepository.findById(teamId)
                .map(TeamMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Team not found with ID: " + teamId));
    }

    @Override
    public List<Team> listTeams(String q) {
        List<TeamEntity> teams;
        if (q == null || q.isEmpty()) {
            teams = teamRepository.findAll(
                    Sort.by(Sort.Direction.DESC, "updatedAt")
            );
        } else {
            teams = teamRepository.findByNameContainingIgnoreCase(q,
                    Sort.by(Sort.Direction.DESC, "updatedAt")
            );
        }
        return teams.stream()
                .map(TeamMapper::toResponse)
                .collect(Collectors.toList());
    }


    public void validate(TeamRequest request) {
        if (request == null) throw new IllegalArgumentException("Team request cannot be null.");
        if (!StringUtils.hasText(request.getName())) throw new IllegalArgumentException("Team name is required.");
        if (!StringUtils.hasText(request.getShortCode())) throw new IllegalArgumentException("Short code is required.");
    }


    @Override
    public void deleteTeam(Long teamId) {
        if (teamId == null || teamId <= 0) throw new IllegalArgumentException("Invalid team ID");
        if (!teamRepository.existsById(teamId)) {
            throw new NoSuchElementException("Team not found with ID: " + teamId);
        }
        teamRepository.deleteById(teamId);
    }

    @Override
    public TeamPlayerInfo getAllPlayerFromTeam(Long teamId) {
        TeamEntity teamEntity = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));

        List<ListPlayerRes> list = teamEntity.getPlayers() != null
                ? teamEntity.getPlayers().stream()
                .map(ListPlayerRes::toShortPlayer)
                .toList()
                : Collections.emptyList();

        Long captainId = teamEntity.getCaptain() != null ? teamEntity.getCaptain().getId() : null;
        Long viceCaptainId = teamEntity.getViceCaptain() != null ? teamEntity.getViceCaptain().getId() : null;

        return new TeamPlayerInfo(list, teamId, captainId, viceCaptainId);
    }


    @Override
    public List<Player> updatePlayers(List<Long> players, Long teamId, Long captainId, Long viceCaptainId) {
        TeamEntity teamEntity = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));

        // Ensure unique player IDs
        Set<Long> uniquePlayerIds = new HashSet<>(players);
        List<PlayerEntity> allById = playerRepository.findAllById(uniquePlayerIds);

        // ✅ Update team players
        teamEntity.setPlayers(allById);

        // ✅ Validate captain and vice-captain
        if (captainId != null) {
            if (!uniquePlayerIds.contains(captainId)) {
                throw new IllegalArgumentException("Captain must be part of the team players");
            }
            playerRepository.findById(captainId)
                    .ifPresent(teamEntity::setCaptain);
        } else {
            teamEntity.setCaptain(null);
        }

        if (viceCaptainId != null) {
            if (!uniquePlayerIds.contains(viceCaptainId)) {
                throw new IllegalArgumentException("Vice-Captain must be part of the team players");
            }
            playerRepository.findById(viceCaptainId)
                    .ifPresent(teamEntity::setViceCaptain);
        } else {
            teamEntity.setViceCaptain(null);
        }

        // ✅ Save updated team
        TeamEntity savedTeam = teamRepository.save(teamEntity);

        return savedTeam.getPlayers().stream()
                .map(PlayerMapper::toPlayer)
                .collect(Collectors.toList());
    }




    private TeamEntity buildTeamEntity(TeamRequest request) {
        return buildTeamEntity(new TeamEntity(), request);
    }

    private TeamEntity buildTeamEntity(TeamEntity entity, TeamRequest request) {
        Optional.ofNullable(request.getName()).ifPresent(entity::setName);
        Optional.ofNullable(request.getShortCode()).ifPresent(entity::setShortCode);
        Optional.ofNullable(request.getLogoUrl()).ifPresent(entity::setLogoUrl);
        Optional.ofNullable(request.getCoach()).ifPresent(entity::setCoach);
        Optional.ofNullable(request.getColorCode()).ifPresent(entity::setColorCode);
        Optional.ofNullable(request.getBgImage()).ifPresent(entity::setBgImage);

        if (!request.getPlayers().isEmpty()) {
            Set<Long> uniquePlayerIds = new HashSet<>(request.getPlayers());
            List<PlayerEntity> allById = playerRepository.findAllById(uniquePlayerIds);
            entity.setPlayers(allById);
            if (request.getCaptainId() != null) {
                playerRepository.findById(request.getCaptainId())
                        .ifPresent(entity::setCaptain);
            }
            if (request.getViceCaptainId() != null) {
                playerRepository.findById(request.getViceCaptainId())
                        .ifPresent(entity::setViceCaptain);
            }
        }
        return entity;
    }

}
