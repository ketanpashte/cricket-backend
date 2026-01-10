package com.fastx.live_score.modules.match.db;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "match_sessions")
public class MatchSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false, unique = true)
    private MatchEntity matchEntity;

    // Team 1 Rate
    private Double team1Min;
    private Double team1Max;

    // Team 2 Rate
    private Double team2Min;
    private Double team2Max;

    // Draw Rate
    private Double drawMin;
    private Double drawMax;

    // Session Score
    private Integer sessionOver;
    private Integer sessionMinScore;
    private Integer sessionMaxScore;

    // Lambi Score
    private Integer lambiMinScore;
    private Integer lambiMaxScore;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
