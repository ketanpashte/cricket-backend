package com.fastx.live_score.modules.liveMatch.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LiveMatchEventEntityRepository extends JpaRepositoryImplementation<LiveMatchEventEntity, UUID> {
    @Query("""
            select l from LiveMatchEventEntity l
            where l.matchEntity.liveMatchId = :liveMatchId
            order by l.lastModifiedDate""")
    List<LiveMatchEventEntity> findByMatchEntity_LiveMatchIdOrderByLastModifiedDateAsc(@Param("liveMatchId") UUID liveMatchId);
}