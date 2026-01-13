package com.fastx.live_score.modules.player.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "players", uniqueConstraints = {
        @UniqueConstraint(name = "uc_playerentity_fullname", columnNames = { "fullName" })
})
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String fullName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String shortName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String nationality;

    @Column(columnDefinition = "TEXT")
    private String role;

    private String battingStyle;
    private String bowlingStyle;

    private int totalRuns;
    private int totalWickets;
    private int totalMatches;

    private boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

}
