package com.fastx.live_score.modules.tournament.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TournamentPointsTableEntityRepository extends JpaRepositoryImplementation<TournamentPointsTableEntity, Long> {
    @Query("select t from TournamentPointsTableEntity t where t.tournament.id = :id and t.team.id = :id1")
    Optional<TournamentPointsTableEntity> findByTournament_IdAndTeam_Id(@Param("id") Long id, @Param("id1") Long id1);
}