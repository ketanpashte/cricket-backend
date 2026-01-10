package com.fastx.live_score.modules.weather;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long> {

    // Find weather info by match ID
    Optional<WeatherInfo> findByMatchId(Long matchId);

    // Delete weather info for a match
    void deleteByMatchId(Long matchId);

    // Check if weather info exists for a match
    boolean existsByMatchId(Long matchId);
}
