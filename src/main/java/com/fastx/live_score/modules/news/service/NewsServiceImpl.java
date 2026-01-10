package com.fastx.live_score.modules.news.service;

import com.fastx.live_score.modules.news.db.NewsEntity;
import com.fastx.live_score.modules.news.db.NewsRepository;
import com.fastx.live_score.modules.news.dto.NewsDto;
import com.fastx.live_score.modules.news.mapper.NewsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Override
    @Transactional
    public NewsDto createNews(NewsDto newsDto) {
        NewsEntity entity = newsMapper.toEntity(newsDto);
        if (entity.getDisplayOrder() == null) {
            Integer maxOrder = newsRepository.findMaxDisplayOrder();
            entity.setDisplayOrder(maxOrder == null ? 1 : maxOrder + 1);
        }
        return newsMapper.toDto(newsRepository.save(entity));
    }

    @Override
    @Transactional
    public NewsDto updateNews(Long id, NewsDto newsDto) {
        NewsEntity entity = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));

        entity.setTitle(newsDto.getTitle());
        entity.setContent(newsDto.getContent());
        entity.setImageUrl(newsDto.getImageUrl());
        // We only update order and published status via specific methods or if intended
        // Here we assume basic details update

        return newsMapper.toDto(newsRepository.save(entity));
    }

    @Override
    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public NewsDto getNews(Long id) {
        return newsRepository.findById(id)
                .map(newsMapper::toDto)
                .orElseThrow(() -> new RuntimeException("News not found"));
    }

    @Override
    public List<NewsDto> getAllNews() {
        return newsRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(newsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NewsDto> getPublishedNews(Pageable pageable) {
        return newsRepository.findByIsPublishedTrueOrderByDisplayOrderAsc(pageable)
                .map(newsMapper::toDto);
    }

    @Override
    @Transactional
    public void publishNews(Long id) {
        NewsEntity entity = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        entity.setPublished(true);
        newsRepository.save(entity);
    }

    @Override
    @Transactional
    public void reorderNews(List<Long> newsIds) {
        AtomicInteger index = new AtomicInteger(1);
        newsIds.forEach(id -> {
            newsRepository.findById(id).ifPresent(news -> {
                news.setDisplayOrder(index.getAndIncrement());
                newsRepository.save(news);
            });
        });
    }
}
