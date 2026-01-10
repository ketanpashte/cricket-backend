package com.fastx.live_score.modules.team;

import com.fastx.live_score.modules.player.entity.PlayerEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
@Data
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String shortCode;

    private String logoUrl;
    private String bgImage;
    private String coach;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<PlayerEntity> players = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captain_id")
    private PlayerEntity captain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vice_captain_id")
    private PlayerEntity viceCaptain;

    private String colorCode;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public PlayerEntity getPlayerById(Long id) {
        return getPlayers().stream().filter(playerEntity -> playerEntity.getId().equals(id))
                .findFirst().orElseThrow();
    }
}
