package com.fastx.live_score.modules.venue.service;

import com.fastx.live_score.modules.venue.db.entity.Venue;
import com.fastx.live_score.modules.venue.db.entity.VenueScoringPattern;
import com.fastx.live_score.modules.venue.db.repository.VenueRepository;
import com.fastx.live_score.modules.venue.db.repository.VenueScoringPatternRepository;
import com.fastx.live_score.modules.venue.dto.VenueScoringDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class VenueScoringService {

    private final VenueScoringPatternRepository repository;
    private final VenueRepository venueRepository;
    private final ModelMapper modelMapper;

    public VenueScoringService(VenueScoringPatternRepository repository, VenueRepository venueRepository, ModelMapper modelMapper) {
        this.repository = repository;
        this.venueRepository = venueRepository;
        this.modelMapper = modelMapper;
    }

    public VenueScoringDto getByVenueId(Long id) {
        return repository.findByVenue_Id(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("VenueScoringPattern not found with id " + id));
    }


    public VenueScoringDto update(Long id, VenueScoringDto dto) {

        Venue venue = venueRepository.findById(id).orElseThrow(() -> new RuntimeException("Venue not found with id " + id));
        VenueScoringPattern existing = repository.findByVenue_Id(id)
                .orElse(VenueScoringPattern.defaultPattern(venue));
        existing.setTotalMatches(dto.getTotalMatches());
        existing.setWinBatFirst(dto.getWinBatFirst());
        existing.setWinBowlSecond(dto.getWinBowlSecond());
        existing.setFirstInningBattingAvgScore(dto.getFirstInningBattingAvgScore());
        existing.setSecondInningBattingAvgScore(dto.getSecondInningBattingAvgScore());
        existing.setHighestTotal(dto.getHighestTotal());
        venue.setScoringPattern(existing);
        existing.setVenue(venue);
        return mapToDto(repository.save(existing));
    }

    private VenueScoringDto mapToDto(VenueScoringPattern entity) {
        return modelMapper.map(entity, VenueScoringDto.class);
    }

}
