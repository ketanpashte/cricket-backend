package com.fastx.live_score.modules.tournament.db;

import com.fastx.live_score.modules.team.TeamEntity;
import com.fastx.live_score.modules.match.db.MatchEntity;
import com.fastx.live_score.modules.tournament.dto.TournamentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "tournaments", indexes = {
        @Index(name = "idx_tournamententity", columnList = "startDate, endDate")
})
@ToString
@RequiredArgsConstructor
public class TournamentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private TournamentStatus tournamentStatus = TournamentStatus.UP_COMING;

    @Column(nullable = false)
    private String tournamentType = "";

    private String seriesFormat;

    private LocalDateTime endDate;
    private String location;
    private String logoUrl;

    @ElementCollection
    private List<String> hostingNation;

    @Column(nullable = false)
    private Integer maxParticipants;

    @ElementCollection
    @CollectionTable(name = "tournament_tags", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<MatchEntity> matches = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tournament_teams", joinColumns = @JoinColumn(name = "tournament_id"), inverseJoinColumns = @JoinColumn(name = "team_id"))
    @ToString.Exclude
    private List<TeamEntity> participatingTeams = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    @ToString.Exclude
    private TeamEntity winner;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isGroup = false;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TournamentGroupEntity> groups = new ArrayList<>();

    @Column(nullable = false)
    private int pointsForWin = 2;

    @Column(nullable = false)
    private int pointsForTie = 1;

    @Column(nullable = false)
    private int pointsForNoResult = 1;

    @Column(nullable = false)
    private int pointsForLoss = 0;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TournamentPointsTableEntity> pointsTable = new ArrayList<>();

    /* Highlights Section */
    private String highlightMostRunsPlayer;
    private String highlightMostRunsValue;
    private String highlightMostWicketsPlayer;
    private String highlightMostWicketsValue;
    private String highlightBestFigurePlayer;
    private String highlightBestFigureValue;

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
            return false;
        TournamentEntity that = (TournamentEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
