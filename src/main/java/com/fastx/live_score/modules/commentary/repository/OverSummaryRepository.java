package com.fastx.live_score.modules.commentary.repository;

import com.fastx.live_score.modules.commentary.entity.OverSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OverSummaryRepository extends JpaRepository<OverSummaryEntity, Long> {
    List<OverSummaryEntity> findByMatchIdOrderByOverNumberDesc(String matchId);
}
