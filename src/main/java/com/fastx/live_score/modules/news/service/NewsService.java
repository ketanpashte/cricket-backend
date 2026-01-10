package com.fastx.live_score.modules.news.service;

import com.fastx.live_score.modules.news.dto.NewsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {
    NewsDto createNews(NewsDto newsDto);

    NewsDto updateNews(Long id, NewsDto newsDto);

    void deleteNews(Long id);

    NewsDto getNews(Long id);

    List<NewsDto> getAllNews();

    Page<NewsDto> getPublishedNews(Pageable pageable);

    void publishNews(Long id);

    void reorderNews(List<Long> newsIds);
}
