package com.fastx.live_score.modules.commentary.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "over_summary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class OverSummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", nullable = false)
    private String matchId;

    @Column(name = "over_number")
    private Integer overNumber;

    @Column(name = "team_total_runs")
    private Integer teamTotalRuns;

    @Column(name = "team_total_wickets")
    private Integer teamTotalWickets;

    @Column(name = "striker_name")
    private String strikerName;

    @Column(name = "striker_runs")
    private Integer strikerRuns;

    @Column(name = "striker_balls")
    private Integer strikerBalls;

    @Column(name = "non_striker_name")
    private String nonStrikerName;

    @Column(name = "non_striker_runs")
    private Integer nonStrikerRuns;

    @Column(name = "non_striker_balls")
    private Integer nonStrikerBalls;

    @Column(name = "bowler_name")
    private String bowlerName;

    @Column(name = "bowler_overs")
    private String bowlerOvers;

    @Column(name = "bowler_maidens")
    private Integer bowlerMaidens;

    @Column(name = "bowler_runs")
    private Integer bowlerRuns;

    @Column(name = "bowler_wickets")
    private Integer bowlerWickets;
}
