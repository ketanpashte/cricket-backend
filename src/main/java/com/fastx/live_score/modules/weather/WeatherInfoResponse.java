package com.fastx.live_score.modules.weather;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherInfoResponse {
    private Long id;
    private String condition;
    private Double temperature;
    private Double humidity;
    private Double windSpeed;
    private Double rainProbability;
    private String icon;
    private Long matchId;

    public static WeatherInfoResponse mapToResponse(WeatherInfo weatherInfo) {
        if (weatherInfo == null) return null;
        return WeatherInfoResponse.builder()
                .id(weatherInfo.getId())
                .condition(weatherInfo.getCondition())
                .temperature(weatherInfo.getTemperature())
                .humidity(weatherInfo.getHumidity())
                .windSpeed(weatherInfo.getWindSpeed())
                .rainProbability(weatherInfo.getRainProbability())
                .icon(weatherInfo.getIcon())
                .matchId(weatherInfo.getMatch().getId())
                .build();
    }
}
