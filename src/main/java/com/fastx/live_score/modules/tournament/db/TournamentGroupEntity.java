package com.fastx.live_score.modules.tournament.db;

import com.fastx.live_score.modules.team.TeamEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournament_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private TournamentEntity tournament;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "group_teams",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<TeamEntity> teams = new ArrayList<>();
}
