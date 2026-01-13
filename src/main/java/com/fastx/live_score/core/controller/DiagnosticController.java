package com.fastx.live_score.core.controller;

import com.fastx.live_score.core.utils.AppResponse;
import com.fastx.live_score.modules.news.db.NewsRepository;
import com.fastx.live_score.modules.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.fastx.live_score.core.config.APiConfig.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/diagnose")
@RequiredArgsConstructor
public class DiagnosticController {

    private final PlayerRepository playerRepository;
    private final NewsRepository newsRepository;

    @GetMapping
    public AppResponse<Map<String, Object>> runDiagnostics() {
        Map<String, Object> results = new HashMap<>();

        // 1. Check Players
        try {
            long count = playerRepository.count();
            results.put("players_count", count);
            results.put("players_status", "OK");
        } catch (Exception e) {
            results.put("players_status", "ERROR: " + e.getMessage());
            results.put("players_error_class", e.getClass().getName());
        }

        // 2. Check News
        try {
            long count = newsRepository.count();
            results.put("news_count", count);
            results.put("news_status", "OK");
        } catch (Exception e) {
            results.put("news_status", "ERROR: " + e.getMessage());
            results.put("news_error_class", e.getClass().getName());
        }

        // 3. Check News Query (Specific suspect)
        try {
            var list = newsRepository.findAllByOrderByDisplayOrderAsc();
            results.put("news_query_list_size", list.size());
            results.put("news_query_status", "OK");
        } catch (Exception e) {
            results.put("news_query_status", "ERROR: " + e.getMessage());
        }

        return AppResponse.success(results);
    }
}
