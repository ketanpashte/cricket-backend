package com.fastx.live_score.modules.news.mapper;

import com.fastx.live_score.modules.news.db.NewsEntity;
import com.fastx.live_score.modules.news.dto.NewsDto;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {

    public NewsEntity toEntity(NewsDto dto) {
        if (dto == null)
            return null;
        return NewsEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .imageUrl(dto.getImageUrl())
                .isPublished(dto.isPublished())
                .displayOrder(dto.getDisplayOrder())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public NewsDto toDto(NewsEntity entity) {
        if (entity == null)
            return null;
        return NewsDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrl(entity.getImageUrl())
                .isPublished(entity.isPublished())
                .displayOrder(entity.getDisplayOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
