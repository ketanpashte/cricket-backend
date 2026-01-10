package com.fastx.live_score.modules.tournament.db;

import com.fastx.live_score.modules.team.TeamEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tournament_points_table", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tournament_id", "team_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentPointsTableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private TournamentEntity tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @Column(nullable = false)
    private int matchesPlayed = 0;

    @Column(nullable = false)
    private int wins = 0;

    @Column(nullable = false)
    private int losses = 0;

    @Column(nullable = false)
    private int ties = 0;

    @Column(nullable = false)
    private int noResults = 0;

    @Column(nullable = false)
    private int points = 0;

    @Column(nullable = false)
    private double netRunRate = 0.0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
