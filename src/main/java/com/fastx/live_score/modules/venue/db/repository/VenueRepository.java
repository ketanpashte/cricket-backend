package com.fastx.live_score.modules.venue.db.repository;

import com.fastx.live_score.modules.venue.db.entity.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VenueRepository extends JpaRepositoryImplementation<Venue, Long> {
    @Query("select v from Venue v where upper(v.country) = upper(:country) order by v.capacity DESC")
    List<Venue> findByCountryIgnoreCaseAllIgnoreCaseOrderByCapacityDesc(@Param("country") String country);

    @Query("select (count(v) > 0) from Venue v where v.name = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT DISTINCT v.country FROM Venue v ORDER BY v.country ASC")
    List<String> findAllDistinctCountries();

    Page<Venue> findByNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrCountryContainingIgnoreCase(
            String name, String city, String country, Pageable pageable
    );
}