package com.fastx.live_score.modules.match.service;

import com.fastx.live_score.modules.liveMatch.response.MatchListItemDto;
import com.fastx.live_score.modules.match.dto.Match;
import com.fastx.live_score.modules.match.dto.MatchInfo;
import com.fastx.live_score.modules.match.dto.MatchRequest;
import com.fastx.live_score.modules.match.dto.MatchStatus;

import java.util.List;
import java.util.UUID;

public interface MatchService {

    void saveMatch(MatchRequest request);

    List<Match> listMatches(MatchStatus matchStatus);

    List<Match> listMatchesByTourId(long tourId);

    Match getMatchById(Long matchId);

    List<MatchListItemDto> listAllMatches();

    MatchListItemDto getMatchScoreById(UUID matchId);

    void deleteMatch(Long matchId);


    void forceRestartMatch(Long matchId);

    MatchInfo getMatchINfo(Long matchId);
}