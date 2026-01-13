package com.fastx.live_score.modules.player.repository;

import com.fastx.live_score.modules.player.entity.PlayerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

        @Query("SELECT p FROM PlayerEntity p WHERE LOWER(p.fullName) " +
                        "LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.shortName) " +
                        "LIKE LOWER(CONCAT('%', :query, '%'))")
        Page<PlayerEntity> searchByName(String query, Pageable pageable);

        @Query("select (count(p) > 0) from PlayerEntity p where p.fullName = :fullName")
        boolean existsByFullName(@Param("fullName") String fullName);

        @Query(value = """
                            SELECT p FROM PlayerEntity p
                            WHERE (
                                    :query IS NULL OR
                                    (LOWER(p.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
                                    OR LOWER(p.shortName) LIKE LOWER(CONCAT('%', :query, '%')))
                                  )
                              AND (:role IS NULL OR p.role = :role)
                              AND (:nationality IS NULL OR LOWER(p.nationality) LIKE LOWER(CONCAT('%', :nationality, '%')))
                        """, countQuery = """
                            SELECT count(p) FROM PlayerEntity p
                            WHERE (
                                    :query IS NULL OR
                                    (LOWER(p.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
                                    OR LOWER(p.shortName) LIKE LOWER(CONCAT('%', :query, '%')))
                                  )
                              AND (:role IS NULL OR p.role = :role)
                              AND (:nationality IS NULL OR LOWER(p.nationality) LIKE LOWER(CONCAT('%', :nationality, '%')))
                        """)
        Page<PlayerEntity> searchPlayers(
                        @Param("query") String query,
                        @Param("role") String role,
                        @Param("nationality") String nationality,
                        Pageable pageable);

}
