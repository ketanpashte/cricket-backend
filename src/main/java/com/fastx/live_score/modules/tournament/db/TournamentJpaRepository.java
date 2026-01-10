package com.fastx.live_score.modules.tournament.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TournamentJpaRepository extends JpaRepository<TournamentEntity, Long> {
    @Query("""
            select (count(t) > 0) from TournamentEntity t inner join t.participatingTeams participatingTeams
            where participatingTeams.id = :id""")
    boolean existsByParticipatingTeams_Id(@Param("id") Long id);


    @Query("select t from TournamentEntity t where upper(t.name) like upper(concat('%', :name, '%'))")
    List<TournamentEntity> findByNameContainsIgnoreCase(@Param("name") String name);
}
