package com.fastx.live_score.modules.venue.controller;

import com.fastx.live_score.core.config.APiConfig;
import com.fastx.live_score.modules.venue.dto.VenueScoringDto;
import com.fastx.live_score.modules.venue.service.VenueScoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APiConfig.API_VERSION_1 + "/venues/scorings")

public class VenueScoringController {

    private final VenueScoringService service;

    public VenueScoringController(VenueScoringService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueScoringDto> getByVenueId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByVenueId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenueScoringDto> update(@PathVariable Long id, @RequestBody VenueScoringDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }
}
