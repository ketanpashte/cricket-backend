package com.fastx.live_score.modules.tournament.service;

import com.fastx.live_score.modules.tournament.dto.Tournament;
import com.fastx.live_score.modules.tournament.dto.TournamentRequest;

import java.util.List;

public interface TournamentService {

    Tournament createNewTournament(TournamentRequest request);

    Tournament updateTournament(Long tournamentId, TournamentRequest request);

    Tournament updateHighlights(Long tournamentId,
            com.fastx.live_score.modules.tournament.dto.TournamentHighlightsRequest request);

    Tournament getTournamentById(Long tournamentId);

    List<Tournament> getAllTournaments();

    List<Tournament> searchTournament(String query);

    void deleteTournament(Long tournamentId);

    void assignWinner(Long tournamentId, Long teamId);
}