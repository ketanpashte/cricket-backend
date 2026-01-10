package com.fastx.live_score.modules.tips;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TipEntityRepository extends JpaRepositoryImplementation<TipEntity, Long> {
    @Query("select t from TipEntity t where t.matchEntity.id = :id")
    List<TipEntity> getTipsPerMatch(@Param("id") Long id, Sort sort);
}