package com.fastx.live_score.modules.match.controller;

import com.fastx.live_score.core.config.APiConfig;
import com.fastx.live_score.core.utils.AppResponse;
import com.fastx.live_score.modules.match.db.MatchEntity;
import com.fastx.live_score.modules.match.db.MatchEntityRepository;
import com.fastx.live_score.modules.match.db.MatchSessionEntity;
import com.fastx.live_score.modules.match.db.MatchSessionRepository;
import com.fastx.live_score.modules.match.dto.MatchSessionDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APiConfig.API_VERSION_1 + "/matches")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MatchSessionController {

    private final MatchSessionRepository matchSessionRepository;
    private final MatchEntityRepository matchEntityRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/{matchId}/session")
    public AppResponse<MatchSessionDto> getMatchSession(@PathVariable Long matchId) {
        MatchSessionEntity session = matchSessionRepository.findByMatchEntityId(matchId)
                .orElse(MatchSessionEntity.builder().build()); // Return empty if not found

        MatchSessionDto dto = modelMapper.map(session, MatchSessionDto.class);
        dto.setMatchId(matchId);
        return AppResponse.success(dto);
    }

    @PostMapping("/{matchId}/session")
    @Transactional
    public AppResponse<MatchSessionDto> createOrUpdateSession(
            @PathVariable Long matchId,
            @RequestBody MatchSessionDto dto) {

        MatchEntity match = matchEntityRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        MatchSessionEntity session = matchSessionRepository.findByMatchEntityId(matchId)
                .orElse(new MatchSessionEntity());

        session.setMatchEntity(match);

        // Update fields if they are not null in DTO?
        // Or overwrite assuming full update?
        // The requirements suggest different buttons update different parts, but
        // requested "one table".
        // A full DTO update is safest for "Submit" button which seems to send all rate
        // data.
        // But "Update Sess Score" sends session data.
        // We will map non-null fields or just map all.
        // Since React usually sends full state or we can handle specific logic here.
        // For simplicity, let's allow partial updates if we check for nulls, or full
        // overwrite.
        // Given buttons are separate, frontend might send different payloads.
        // Let's assume the frontend sends the relevant fields.

        if (dto.getTeam1Min() != null)
            session.setTeam1Min(dto.getTeam1Min());
        if (dto.getTeam1Max() != null)
            session.setTeam1Max(dto.getTeam1Max());
        if (dto.getTeam2Min() != null)
            session.setTeam2Min(dto.getTeam2Min());
        if (dto.getTeam2Max() != null)
            session.setTeam2Max(dto.getTeam2Max());
        if (dto.getDrawMin() != null)
            session.setDrawMin(dto.getDrawMin());
        if (dto.getDrawMax() != null)
            session.setDrawMax(dto.getDrawMax());

        if (dto.getSessionOver() != null)
            session.setSessionOver(dto.getSessionOver());
        if (dto.getSessionMinScore() != null)
            session.setSessionMinScore(dto.getSessionMinScore());
        if (dto.getSessionMaxScore() != null)
            session.setSessionMaxScore(dto.getSessionMaxScore());

        if (dto.getLambiMinScore() != null)
            session.setLambiMinScore(dto.getLambiMinScore());
        if (dto.getLambiMaxScore() != null)
            session.setLambiMaxScore(dto.getLambiMaxScore());

        MatchSessionEntity saved = matchSessionRepository.save(session);
        MatchSessionDto responseDto = modelMapper.map(saved, MatchSessionDto.class);
        responseDto.setMatchId(matchId);

        return AppResponse.success(responseDto);
    }

    @DeleteMapping("/{matchId}/session/rate")
    @Transactional
    public AppResponse<MatchSessionDto> removeRate(@PathVariable Long matchId) {
        MatchSessionEntity session = matchSessionRepository.findByMatchEntityId(matchId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setTeam1Min(null);
        session.setTeam1Max(null);
        session.setTeam2Min(null);
        session.setTeam2Max(null);
        session.setDrawMin(null);
        session.setDrawMax(null);

        MatchSessionEntity saved = matchSessionRepository.save(session);
        MatchSessionDto responseDto = modelMapper.map(saved, MatchSessionDto.class);
        responseDto.setMatchId(matchId);

        return AppResponse.success(responseDto);
    }

    @PostMapping("/{matchId}/session/reset-score")
    @Transactional
    public AppResponse<MatchSessionDto> resetSessionScore(@PathVariable Long matchId) {
        MatchSessionEntity session = matchSessionRepository.findByMatchEntityId(matchId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setSessionOver(0);
        session.setSessionMinScore(0);
        session.setSessionMaxScore(0);

        MatchSessionEntity saved = matchSessionRepository.save(session);
        MatchSessionDto responseDto = modelMapper.map(saved, MatchSessionDto.class);
        responseDto.setMatchId(matchId);

        return AppResponse.success(responseDto);
    }
}
