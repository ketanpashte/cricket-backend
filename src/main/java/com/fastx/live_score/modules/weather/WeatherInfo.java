package com.fastx.live_score.modules.weather;

import com.fastx.live_score.modules.match.db.MatchEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_info", indexes = {
        @Index(name = "idx_weatherinfo_match_id", columnList = "match_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String condition; // Sunny, Cloudy, Rainy
    private Double temperature; // °C
    private Double humidity; // %
    private Double windSpeed; // km/h
    private Double rainProbability; // % chance of rain
    private String icon;

    private LocalDateTime weatherTime;

    @OneToOne
    @JoinColumn(name = "match_id")
    private MatchEntity match;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
