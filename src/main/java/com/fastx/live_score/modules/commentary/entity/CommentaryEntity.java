package com.fastx.live_score.modules.commentary.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "match_commentary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CommentaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", nullable = false)
    private String matchId;

    @Column(name = "over_number")
    private Integer overNumber;

    @Column(name = "ball_number")
    private Integer ballNumber;

    @Column(name = "commentary_text", columnDefinition = "TEXT")
    private String commentaryText;

    @Column(name = "bowler_name")
    private String bowlerName;

    @Column(name = "batter_name")
    private String batterName;

    @Column(name = "runs")
    private String runs;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
