package com.fastx.live_score.modules.commentary.repository;

import com.fastx.live_score.modules.commentary.entity.CommentaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaryRepository extends JpaRepository<CommentaryEntity, Long> {
    List<CommentaryEntity> findByMatchIdOrderByOverNumberDescBallNumberDesc(String matchId);
}
