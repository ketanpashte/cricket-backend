package com.fastx.live_score.modules.venue.controller;

import com.fastx.live_score.core.config.APiConfig;
import com.fastx.live_score.modules.venue.dto.VenueRequest;
import com.fastx.live_score.modules.venue.dto.VenueResponse;
import com.fastx.live_score.modules.venue.service.VenueService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(APiConfig.API_VERSION_1 + "/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @PostMapping
    public ResponseEntity<VenueResponse> createVenue(@RequestBody VenueRequest request) {
        return ResponseEntity.ok(venueService.createVenue(request));
    }

    @GetMapping
    public ResponseEntity<List<VenueResponse>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    @GetMapping("findByCountry/{country}")
    public ResponseEntity<List<VenueResponse>> findByCountry(@PathVariable String country) {
        return ResponseEntity.ok(venueService.findByCountry(country));
    }

    @GetMapping("/getAvailableCountries")
    public ResponseEntity<List<String>> getAvailableCountries() {
        return ResponseEntity.ok(venueService.getAvailableCountries());
    }


    @GetMapping("/search")
    public Page<VenueResponse> searchVenues(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return venueService.searchVenues(keyword, page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getVenueById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueResponse> updateVenue(@PathVariable Long id, @RequestBody VenueRequest request) {
        return ResponseEntity.ok(venueService.updateVenue(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.ok("Venue deleted successfully");
    }

    @PostMapping("/import")
    public ResponseEntity<String> importVenues(@RequestParam("file") MultipartFile file) {
        venueService.importVenues(file);
        return ResponseEntity.ok("Venues imported successfully!");
    }

    @GetMapping("/export")
    public void exportVenues(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=venues.csv");
        venueService.exportVenues(response.getOutputStream());
    }
}
