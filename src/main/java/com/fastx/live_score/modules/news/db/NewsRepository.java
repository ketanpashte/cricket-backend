package com.fastx.live_score.modules.news.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    Page<NewsEntity> findByIsPublishedTrueOrderByDisplayOrderAsc(Pageable pageable);

    @Query("SELECT MAX(n.displayOrder) FROM NewsEntity n")
    Integer findMaxDisplayOrder();

    List<NewsEntity> findAllByOrderByDisplayOrderAsc();
}
