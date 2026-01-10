package com.fastx.live_score.modules.match.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchSessionRepository extends JpaRepository<MatchSessionEntity, Long> {
    Optional<MatchSessionEntity> findByMatchEntityId(Long matchId);
}
