package com.fastx.live_score.modules.match.db;

import com.fastx.live_score.modules.match.dto.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchEntityRepository extends JpaRepository<MatchEntity, Long> {

    @Query("select m from MatchEntity m where m.liveMatchId = :liveMatchId")
    Optional<MatchEntity> findByLiveMatchId(@Param("liveMatchId") UUID liveMatchId);

    List<MatchEntity> findByTournament_Id(Long tournamentId);

    List<MatchEntity> findByMatchStatus(MatchStatus matchStatus);
}
