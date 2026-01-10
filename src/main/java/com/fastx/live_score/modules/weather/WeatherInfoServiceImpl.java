package com.fastx.live_score.modules.weather;

import com.fastx.live_score.modules.match.db.MatchEntity;
import com.fastx.live_score.modules.match.db.MatchEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.fastx.live_score.modules.weather.WeatherInfoResponse.mapToResponse;

@Service
@RequiredArgsConstructor
public class WeatherInfoServiceImpl implements WeatherInfoService {

    private final WeatherInfoRepository weatherInfoRepository;
    private final MatchEntityRepository matchRepository;

    /**
     * Create or Update weather info for a match.
     * If it exists → Update, else → Create new
     */
    @Override
    public WeatherInfoResponse saveOrUpdateWeather(Long matchId, WeatherInfoRequest request) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with ID: " + matchId));

        WeatherInfo weatherInfo = weatherInfoRepository.findByMatchId(matchId)
                .orElse(WeatherInfo.builder().match(match).build());

        weatherInfo.setCondition(request.getCondition());
        weatherInfo.setTemperature(request.getTemperature());
        weatherInfo.setHumidity(request.getHumidity());
        weatherInfo.setWindSpeed(request.getWindSpeed());
        weatherInfo.setRainProbability(request.getRainProbability());
        weatherInfo.setIcon(request.getIcon());
        weatherInfo.setWeatherTime(LocalDateTime.now());

        WeatherInfo savedWeather = weatherInfoRepository.save(weatherInfo);

        return mapToResponse(savedWeather);
    }

    @Override
    public WeatherInfoResponse getWeatherForMatch(Long matchId) {
        WeatherInfo weatherInfo = weatherInfoRepository.findByMatchId(matchId)
                .orElseThrow(() -> new RuntimeException("No weather info found for match ID: " + matchId));
        return mapToResponse(weatherInfo);
    }

    @Override
    public void deleteWeather(Long matchId) {
        if (!weatherInfoRepository.existsByMatchId(matchId)) {
            throw new RuntimeException("No weather info found for match ID: " + matchId);
        }
        weatherInfoRepository.deleteByMatchId(matchId);
    }


}
