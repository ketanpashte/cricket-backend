package com.fastx.live_score.modules.tournament.service;

import com.fastx.live_score.modules.tournament.db.TournamentPointsTableEntity;
import org.example.Match;

import java.util.List;

public interface PointsTableService {


    void updatePointsTable(Long tournamentId, Match match);

    List<TournamentPointsTableEntity> getPointsTable(Long tournamentId);


    void resetPointsTable(Long tournamentId);
}
