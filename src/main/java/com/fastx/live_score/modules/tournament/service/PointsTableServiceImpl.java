package com.fastx.live_score.modules.tournament.service;

import com.fastx.live_score.modules.tournament.db.TournamentEntity;
import com.fastx.live_score.modules.tournament.db.TournamentJpaRepository;
import com.fastx.live_score.modules.tournament.db.TournamentPointsTableEntity;
import com.fastx.live_score.modules.tournament.db.TournamentPointsTableEntityRepository;
import lombok.RequiredArgsConstructor;
import org.example.*;

import java.util.List;

@RequiredArgsConstructor
public class PointsTableServiceImpl implements PointsTableService {
    private final TournamentJpaRepository tournamentRepository;
    private final TournamentPointsTableEntityRepository pointsRepository;

    @Override
    public void updatePointsTable(Long tournamentId, Match match) {
        MatchResult result = match.result();
        if (result == null) {
            return;
        }

        TournamentEntity tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        ImmutableList<LineUp<?>> teams = match.teams();
        if (teams.size() != 2) {
            throw new IllegalStateException("Expected exactly 2 teams in the match");
        }

        SimpleLineUp teamOne = (SimpleLineUp) teams.get(0);
        SimpleLineUp teamTwo = (SimpleLineUp) teams.get(1);

        TournamentPointsTableEntity entryOne = getPointsEntry(tournamentId, teamOne.getId());
        TournamentPointsTableEntity entryTwo = getPointsEntry(tournamentId, teamTwo.getId());

        entryOne.setMatchesPlayed(entryOne.getMatchesPlayed() + 1);
        entryTwo.setMatchesPlayed(entryTwo.getMatchesPlayed() + 1);


        switch (result.resultType()) {

            case WON, AWARDED, CONCEDED -> {
                SimpleLineUp winner = (SimpleLineUp) result.winningTeam();
                Score winningTeamScore = match.scoredByTeam(winner);
                Score loosingTeamScore = match.scoredByTeam(match.otherTeam(winner));
                double nrr = winningTeamScore.runsPerOver().value() / loosingTeamScore.runsPerOver().value();
                if (winner == null) throw new IllegalStateException("Winner must not be null for a WON result");

                if (winner.sameTeam(teamOne)) {
                    updateScorecard(tournament, entryTwo, entryOne, nrr);
                } else {
                    updateScorecard(tournament, entryOne, entryTwo, nrr);
                }
            }
            case TIED -> {
                entryOne.setTies(entryOne.getTies() + 1);
                entryTwo.setTies(entryTwo.getTies() + 1);
                entryOne.setPoints(entryOne.getPoints() + tournament.getPointsForTie());
                entryTwo.setPoints(entryTwo.getPoints() + tournament.getPointsForTie());
            }
            case DRAWN, NO_RESULT, ABANDONED -> {
                entryOne.setNoResults(entryOne.getNoResults() + 1);
                entryTwo.setNoResults(entryTwo.getNoResults() + 1);
                entryOne.setPoints(entryOne.getPoints() + tournament.getPointsForNoResult());
                entryTwo.setPoints(entryTwo.getPoints() + tournament.getPointsForNoResult());
            }
        }


        pointsRepository.save(entryOne);
        pointsRepository.save(entryTwo);


    }

    private void updateScorecard(TournamentEntity tournament, TournamentPointsTableEntity loosingTeam,
                                 TournamentPointsTableEntity winningTeam, double nrr) {
        winningTeam.setWins(winningTeam.getWins() + 1);
        winningTeam.setPoints(winningTeam.getPoints() + tournament.getPointsForWin());
        winningTeam.setNetRunRate(winningTeam.getNetRunRate() + nrr);
        loosingTeam.setLosses(loosingTeam.getLosses() + 1);
        loosingTeam.setPoints(loosingTeam.getPoints() + tournament.getPointsForLoss());
        loosingTeam.setNetRunRate(loosingTeam.getNetRunRate() - nrr);
    }

    @Override
    public List<TournamentPointsTableEntity> getPointsTable(Long tournamentId) {
        return List.of();
    }

    @Override
    public void resetPointsTable(Long tournamentId) {

    }

    private TournamentPointsTableEntity getPointsEntry(Long tournamentId, Long teamId) {
        return pointsRepository.findByTournament_IdAndTeam_Id(tournamentId, teamId)
                .orElseThrow(() -> new RuntimeException("Points entry not found for team " + teamId));
    }
}
