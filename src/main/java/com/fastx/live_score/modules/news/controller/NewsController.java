package com.fastx.live_score.modules.news.controller;

import com.fastx.live_score.core.utils.AppResponse;
import com.fastx.live_score.modules.news.dto.NewsDto;
import com.fastx.live_score.modules.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fastx.live_score.core.config.APiConfig.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "News Management APIs")
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    @Operation(summary = "Create news")
    public AppResponse<NewsDto> createNews(@RequestBody NewsDto newsDto) {
        return AppResponse.success(newsService.createNews(newsDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update news")
    public AppResponse<NewsDto> updateNews(@PathVariable Long id, @RequestBody NewsDto newsDto) {
        return AppResponse.success(newsService.updateNews(id, newsDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete news")
    public AppResponse<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return AppResponse.success("News deleted successfully", null);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get news by id")
    public AppResponse<NewsDto> getNews(@PathVariable Long id) {
        return AppResponse.success(newsService.getNews(id));
    }

    @GetMapping
    @Operation(summary = "Get all news (Admin)")
    public AppResponse<List<NewsDto>> getAllNews() {
        return AppResponse.success(newsService.getAllNews());
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish news")
    public AppResponse<Void> publishNews(@PathVariable Long id) {
        newsService.publishNews(id);
        return AppResponse.success("News published successfully", null);
    }

    @PostMapping("/reorder")
    @Operation(summary = "Reorder news")
    public AppResponse<Void> reorderNews(@RequestBody List<Long> newsIds) {
        newsService.reorderNews(newsIds);
        return AppResponse.success("News reordered successfully", null);
    }

    @GetMapping("/public")
    @Operation(summary = "Get published news (Mobile App)")
    public AppResponse<Page<NewsDto>> getPublishedNews(Pageable pageable) {
        return AppResponse.success(newsService.getPublishedNews(pageable));
    }
}
