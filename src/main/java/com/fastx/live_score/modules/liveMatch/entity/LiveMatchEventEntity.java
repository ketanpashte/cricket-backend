package com.fastx.live_score.modules.liveMatch.entity;

import com.fastx.live_score.modules.match.db.MatchEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "live_match_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiveMatchEventEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_entity_id", nullable = false)
    private MatchEntity matchEntity;

    private String eventName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "event_data", columnDefinition = "json", nullable = false)
    private String eventData;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

}
