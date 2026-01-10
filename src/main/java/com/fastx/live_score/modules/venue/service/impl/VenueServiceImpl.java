package com.fastx.live_score.modules.venue.service.impl;

import com.fastx.live_score.modules.venue.db.entity.VenueScoringPattern;
import com.fastx.live_score.modules.venue.dto.VenueRequest;
import com.fastx.live_score.modules.venue.dto.VenueResponse;
import com.fastx.live_score.modules.venue.db.entity.Venue;
import com.fastx.live_score.modules.venue.db.repository.VenueRepository;
import com.fastx.live_score.modules.venue.service.VenueService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.fastx.live_score.modules.venue.dto.VenueResponse.mapToResponse;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    @Override
    public VenueResponse createVenue(VenueRequest request) {
        if (venueRepository.existsByName(request.getName())) {
            throw new RuntimeException("Venue already exists with name: " + request.getName());
        }
        Venue venue = Venue.builder()
                .name(request.getName())
                .city(request.getCity())
                .country(request.getCountry())
                .address(request.getAddress())
                .capacity(request.getCapacity())
                .isIndoor(request.getIsIndoor())
                .pitchType(request.getPitchType())
                .build();
        venue.setScoringPattern(VenueScoringPattern.defaultPattern(venue));
        venueRepository.save(venue);
        return mapToResponse(venue);
    }

    @Override
    public List<VenueResponse> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(VenueResponse::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VenueResponse> findByCountry(String country) {
        return venueRepository.findByCountryIgnoreCaseAllIgnoreCaseOrderByCapacityDesc(country)
                .stream().map(VenueResponse::mapToResponse).toList();
    }

    @Override
    public List<String> getAvailableCountries() {
        return venueRepository.findAllDistinctCountries();
    }

    @Override
    public Page<VenueResponse> searchVenues(String keyword, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Venue> venuePage;
        if (keyword == null || keyword.isEmpty()) {
            venuePage = venueRepository.findAll(pageable);
        } else {
            venuePage = venueRepository
                    .findByNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrCountryContainingIgnoreCase(
                            keyword, keyword, keyword, pageable
                    );
        }

        return venuePage.map(VenueResponse::mapToResponse);
    }

    @Override
    public VenueResponse getVenueById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        return mapToResponse(venue);
    }

    @Override
    public VenueResponse updateVenue(Long id, VenueRequest request) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        venue.setName(request.getName());
        venue.setCity(request.getCity());
        venue.setCountry(request.getCountry());
        venue.setAddress(request.getAddress());
        venue.setCapacity(request.getCapacity());
        venue.setIsIndoor(request.getIsIndoor());
        venue.setPitchType(request.getPitchType());

        venueRepository.save(venue);
        return mapToResponse(venue);
    }

    @Override
    public void deleteVenue(Long id) {
        venueRepository.deleteById(id);
    }

    @Override
    public void importVenues(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            CsvToBean<VenueRequest> csvToBean = new CsvToBeanBuilder<VenueRequest>(reader)
                    .withType(VenueRequest.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<VenueRequest> venues = csvToBean.parse();

            List<VenueRequest> uniqueVenues = venues.stream()
                    .filter(v -> !venueRepository.existsByName(v.getName()))
                    .collect(Collectors.toList());

            saveVenues(uniqueVenues);
        } catch (Exception e) {
            throw new RuntimeException("CSV import failed: " + e.getMessage(), e);
        }
    }

    private void saveVenues(List<VenueRequest> venueRequests) {
        List<Venue> venues = venueRequests.stream()
                .map(req -> Venue.builder()
                        .name(req.getName())
                        .city(req.getCity())
                        .country(req.getCountry())
                        .address(req.getAddress())
                        .capacity(req.getCapacity())
                        .isIndoor(req.getIsIndoor())
                        .pitchType(req.getPitchType())
                        .build())
                .collect(Collectors.toList());

        venueRepository.saveAll(venues);
    }

    @Override
    public void exportVenues(OutputStream outputStream) {
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            writer.writeNext(new String[]{"Name", "City", "Country", "Address", "Capacity", "IsIndoor", "PitchType"});

            List<Venue> venues = venueRepository.findAll();
            for (Venue venue : venues) {
                writer.writeNext(new String[]{
                        venue.getName(),
                        venue.getCity(),
                        venue.getCountry(),
                        venue.getAddress() != null ? venue.getAddress() : "",
                        venue.getCapacity() != null ? venue.getCapacity().toString() : "",
                        venue.getIsIndoor() != null ? venue.getIsIndoor().toString() : "false",
                        venue.getPitchType() != null ? venue.getPitchType() : ""
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("CSV export failed: " + e.getMessage(), e);
        }
    }
}
