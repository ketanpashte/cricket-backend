package com.fastx.live_score.modules.match.db;

import com.fastx.live_score.modules.liveMatch.entity.LiveMatchEventEntity;
import com.fastx.live_score.modules.match.dto.MatchStatus;
import com.fastx.live_score.modules.team.TeamEntity;
import com.fastx.live_score.modules.tips.TipEntity;
import com.fastx.live_score.modules.tournament.db.TournamentEntity;
import com.fastx.live_score.modules.venue.db.entity.Venue;
import com.fastx.live_score.modules.weather.WeatherInfo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "matches", indexes = {
        @Index(name = "idx_matchentity_live_match_id", columnList = "live_match_id")
})
@Getter
@Setter
@RequiredArgsConstructor
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournamentId")
    @ToString.Exclude
    private TournamentEntity tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_a_id", nullable = false)
    @ToString.Exclude
    private TeamEntity teamEntityA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_b_id", nullable = false)
    @ToString.Exclude
    private TeamEntity teamEntityB;

    @Column(nullable = false)
    private int totalOvers;
    @Column(nullable = false)
    private MatchStatus matchStatus = MatchStatus.NOT_STARTED;

    @ManyToOne(fetch = FetchType.LAZY)
    private Venue venue;

    private int tossWinner;

    private String electedTo;

    private int winningTeam;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(name = "live_match_id", unique = true)
    private UUID liveMatchId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "matchEntity", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<TipEntity> tips = new ArrayList<>();

    @OneToMany(mappedBy = "matchEntity", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LiveMatchEventEntity> liveMatchEventEntities = new ArrayList<>();

    @Column(name = "ground_umpire_1")
    private String groundUmpire1;

    @Column(name = "ground_umpire_2")
    private String groundUmpire2;

    @Column(name = "third_umpire")
    private String thirdUmpire;

    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private WeatherInfo weatherInfo;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        MatchEntity that = (MatchEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
