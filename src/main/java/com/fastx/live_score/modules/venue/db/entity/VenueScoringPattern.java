package com.fastx.live_score.modules.venue.db.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Entity
public class VenueScoringPattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    Integer totalMatches = 0;
    Integer winBatFirst = 0;
    Integer winBowlSecond = 0;
    Integer firstInningBattingAvgScore = 0;
    Integer secondInningBattingAvgScore = 0;
    Integer highestTotal = 0;

    public static VenueScoringPattern defaultPattern(Venue venue) {
        VenueScoringPattern pattern = new VenueScoringPattern();
        pattern.setTotalMatches(0);
        pattern.setWinBatFirst(0);
        pattern.setWinBowlSecond(0);
        pattern.setFirstInningBattingAvgScore(0);
        pattern.setSecondInningBattingAvgScore(0);
        pattern.setHighestTotal(0);
        pattern.setVenue(venue);
        return pattern;
    }

    @CreationTimestamp
    Instant createdAt;

    @UpdateTimestamp
    Instant updatedAt;

    @OneToOne(mappedBy = "scoringPattern", orphanRemoval = true)
    private Venue venue;

}
