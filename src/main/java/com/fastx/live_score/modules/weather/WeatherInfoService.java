package com.fastx.live_score.modules.weather;

public interface WeatherInfoService {
    WeatherInfoResponse saveOrUpdateWeather(Long matchId, WeatherInfoRequest request);
    WeatherInfoResponse getWeatherForMatch(Long matchId);
    void deleteWeather(Long matchId);
}
