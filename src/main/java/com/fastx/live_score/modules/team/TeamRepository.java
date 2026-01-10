package com.fastx.live_score.modules.team;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    List<TeamEntity> findByNameContainingIgnoreCase(String name, Sort sort);
}
