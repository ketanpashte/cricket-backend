package com.fastx.live_score.modules.match.dto;

import com.fastx.live_score.mapper.TeamMapper;
import com.fastx.live_score.modules.match.db.MatchEntity;
import com.fastx.live_score.modules.team.ListTeamRes;
import com.fastx.live_score.modules.venue.db.entity.Venue;
import com.fastx.live_score.modules.venue.dto.VenueResponse;
import com.fastx.live_score.modules.venue.dto.VenueScoringDto;
import com.fastx.live_score.modules.weather.WeatherInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchInfo {
    private Long id;
    private ListTeamRes teamA;
    private ListTeamRes teamB;
    private int totalOvers;
    private Long venueId;
    private MatchStatus matchStatus;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UUID liveMatchId;
    private String groundUmpire1;
    private String groundUmpire2;
    private String thirdUmpire;

    private WeatherInfoResponse weatherInfoResponse;
    private VenueResponse venueResponse;
    private VenueScoringDto venueScoringDto;

    public static MatchInfo createFromMatch(MatchEntity match) {
        if (match == null) {
            return null;
        }
        Venue venue = match.getVenue();
        return MatchInfo.builder()
                .id(match.getId())
                .teamA(ListTeamRes.from(TeamMapper.toResponse(match.getTeamEntityA())))
                .teamB(ListTeamRes.from(TeamMapper.toResponse(match.getTeamEntityB())))
                .totalOvers(match.getTotalOvers())
                .matchStatus(match.getMatchStatus())
                .startTime(match.getStartTime())
                .endTime(match.getEndTime())
                .liveMatchId(match.getLiveMatchId())
                .weatherInfoResponse(WeatherInfoResponse.mapToResponse(match.getWeatherInfo()))
                .venueResponse(VenueResponse.mapToResponse(venue))
                .groundUmpire1(match.getGroundUmpire1())
                .groundUmpire2(match.getGroundUmpire2())
                .thirdUmpire(match.getThirdUmpire())
                .build();
    }
}