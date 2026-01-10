package com.fastx.live_score.modules.weather;

import lombok.Data;

@Data
public class WeatherInfoRequest {
    private String condition;
    private Double temperature;
    private Double humidity;
    private Double windSpeed;
    private Double rainProbability;
    private String icon;
}
