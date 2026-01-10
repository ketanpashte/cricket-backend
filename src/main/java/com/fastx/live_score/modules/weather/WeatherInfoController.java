package com.fastx.live_score.modules.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.fastx.live_score.core.config.APiConfig.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/weather")
@RequiredArgsConstructor
public class WeatherInfoController {

    private final WeatherInfoService weatherInfoService;

    /**
     * Create or Update weather info for a match
     */
    @PostMapping("/{matchId}")
    public ResponseEntity<WeatherInfoResponse> saveOrUpdateWeather(
            @PathVariable Long matchId,
            @RequestBody WeatherInfoRequest request) {

        WeatherInfoResponse response = weatherInfoService.saveOrUpdateWeather(matchId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get weather info for a match
     */
    @GetMapping("/{matchId}")
    public ResponseEntity<WeatherInfoResponse> getWeather(@PathVariable Long matchId) {
        WeatherInfoResponse response = weatherInfoService.getWeatherForMatch(matchId);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete weather info for a match
     */
    @DeleteMapping("/{matchId}")
    public ResponseEntity<Void> deleteWeather(@PathVariable Long matchId) {
        weatherInfoService.deleteWeather(matchId);
        return ResponseEntity.noContent().build();
    }
}
