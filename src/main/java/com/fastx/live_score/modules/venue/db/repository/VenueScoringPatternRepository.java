package com.fastx.live_score.modules.venue.db.repository;

import com.fastx.live_score.modules.venue.db.entity.VenueScoringPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VenueScoringPatternRepository extends JpaRepository<VenueScoringPattern, Long> {
    @Query("select v from VenueScoringPattern v where v.venue.id = :id")
    Optional<VenueScoringPattern> findByVenue_Id(@Param("id") Long id);
}
