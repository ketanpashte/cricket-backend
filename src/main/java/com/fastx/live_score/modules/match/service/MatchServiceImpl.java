package com.fastx.live_score.modules.match.service;

import com.fastx.live_score.core.exception.AppException;
import com.fastx.live_score.core.file.LiveMatchFileRepository;
import com.fastx.live_score.modules.liveMatch.response.MatchListItemDto;
import com.fastx.live_score.modules.match.dto.Match;
import com.fastx.live_score.modules.match.dto.MatchInfo;
import com.fastx.live_score.modules.match.dto.MatchRequest;
import com.fastx.live_score.modules.match.dto.MatchStatus;
import com.fastx.live_score.modules.match.db.MatchEntity;
import com.fastx.live_score.modules.match.db.MatchEntityRepository;
import com.fastx.live_score.modules.team.TeamEntity;
import com.fastx.live_score.modules.tournament.db.TournamentEntity;
import com.fastx.live_score.mapper.MatchMapper;
import com.fastx.live_score.modules.team.TeamRepository;
import com.fastx.live_score.modules.tournament.db.TournamentJpaRepository;
import com.fastx.live_score.modules.venue.db.repository.VenueRepository;
import com.fastx.live_score.modules.venue.dto.VenueScoringDto;
import org.example.MatchControl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchEntityRepository matchEntityRepository;
    private final TournamentJpaRepository tournamentJpaRepository;
    private final TeamRepository teamRepository;
    private final LiveMatchFileRepository liveMatchFileRepository;
    private final VenueRepository venueRepository;

    private final ModelMapper modelMapper;

    @Override
    public void saveMatch(MatchRequest request) {
        MatchEntity matchEntity = new MatchEntity();
        if (request.getMatchId() != null) {
            matchEntity = matchEntityRepository.findById(request.getMatchId()).orElse(new MatchEntity());
        }
        matchEntityRepository.save(getMatchEntity(matchEntity, request));
    }

    @Override
    public List<Match> listMatches(MatchStatus matchStatus) {
        if (matchStatus == null) {
            return matchEntityRepository.findAll().stream().map(MatchMapper::toMatch).toList();
        }
        List<MatchEntity> byMatchStatus = matchEntityRepository.findByMatchStatus(matchStatus);
        return byMatchStatus.stream().map(MatchMapper::toMatch).toList();
    }

    @Override
    public List<Match> listMatchesByTourId(long tourId) {
        List<MatchEntity> matchEntities = matchEntityRepository.findByTournament_Id(tourId);
        return matchEntities.stream().map(MatchMapper::toMatch).toList();
    }

    private MatchEntity getMatchEntity(MatchEntity entity, MatchRequest request) {
        if (request.getTournamentId() != null) {
            TournamentEntity tournamentEntity = tournamentJpaRepository.findById(request.getTournamentId()).orElseThrow();
            entity.setTournament(tournamentEntity);
        }

        entity.setGroundUmpire1(request.getGroundUmpire1());
        entity.setGroundUmpire2(request.getGroundUmpire2());
        entity.setThirdUmpire(request.getThirdUmpire());

        if (request.getTeamAId() != null) {
            TeamEntity teamEntity = teamRepository.findById(request.getTeamAId()).orElseThrow();
            entity.setTeamEntityA(teamEntity);
        }
        if (request.getTeamBId() != null) {
            TeamEntity teamEntity = teamRepository.findById(request.getTeamBId()).orElseThrow();
            entity.setTeamEntityB(teamEntity);
        }
        if (Objects.equals(request.getTeamAId(), request.getTeamBId())) {
            throw new AppException("Cant have 2 same teams");
        }
        entity.setTotalOvers(request.getTotalOvers());

        if (request.getStartTime() != 0) {
            entity.setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getStartTime()), ZoneId.systemDefault()));
        }
        if (request.getEndTime() != 0) {
            entity.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getEndTime()), ZoneId.systemDefault()));
        }


        if (request.getVenueId() != null) {
            venueRepository.findById(request.getVenueId()).ifPresent(entity::setVenue);
        }

        return entity;
    }


    @Override
    public Match getMatchById(Long matchId) {
        MatchEntity matchEntity = matchEntityRepository.findById(matchId).orElseThrow();
        return MatchMapper.toMatch(matchEntity);
    }

    @Override
    public List<MatchListItemDto> listAllMatches() {
        return matchEntityRepository.findAll().stream().map(matchEntity -> {
            MatchControl matchControl = null;
            if (matchEntity.getLiveMatchId() != null) {
                matchControl = liveMatchFileRepository.findById(matchEntity.getLiveMatchId().toString());
            }
            return MatchListItemDto.fromMatchController(matchEntity, matchControl);
        }).toList();
    }

    @Override
    public MatchListItemDto getMatchScoreById(UUID matchId) {
        MatchEntity matchEntity = matchEntityRepository.findByLiveMatchId(matchId).orElseThrow();
        MatchControl matchControl = null;
        if (matchEntity.getLiveMatchId() != null) {
            matchControl = liveMatchFileRepository.findById(matchEntity.getLiveMatchId().toString());
        }
        return MatchListItemDto.fromMatchController(matchEntity, matchControl);
    }

    @Override
    public void deleteMatch(Long matchId) {
        matchEntityRepository.deleteById(matchId);
    }

    @Override
    public void forceRestartMatch(Long matchId) {
        MatchEntity matchEntity = matchEntityRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        matchEntity.setLiveMatchId(null);
        matchEntity.setMatchStatus(MatchStatus.NOT_STARTED);
        matchEntityRepository.save(matchEntity);

    }

    @Override
    public MatchInfo getMatchINfo(Long matchId) {
        MatchEntity matchEntity = matchEntityRepository.findById(matchId).orElseThrow();
        MatchInfo fromMatch = MatchInfo.createFromMatch(matchEntity);
        fromMatch.setVenueScoringDto(modelMapper.map(matchEntity.getVenue().getScoringPattern(), VenueScoringDto.class));
        return fromMatch;
    }


}
