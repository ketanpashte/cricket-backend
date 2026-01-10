package com.fastx.live_score.modules.venue.service;

import com.fastx.live_score.modules.venue.dto.VenueRequest;
import com.fastx.live_score.modules.venue.dto.VenueResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;

public interface VenueService {

    VenueResponse createVenue(VenueRequest request);

    List<VenueResponse> getAllVenues();

    List<VenueResponse> findByCountry(String country);

    List<String> getAvailableCountries();

    Page<VenueResponse> searchVenues(String keyword, int page, int size, String sortBy, String direction);

    VenueResponse getVenueById(Long id);

    VenueResponse updateVenue(Long id, VenueRequest request);

    void deleteVenue(Long id);

    void importVenues(MultipartFile file);

    void exportVenues(OutputStream outputStream);
}
