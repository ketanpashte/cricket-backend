package com.fastx.live_score.modules.venue.dto;

import com.fastx.live_score.modules.venue.db.entity.Venue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VenueResponse {
    private Long id;
    private String name;
    private String city;
    private String country;
    private String address;
    private Integer capacity;
    private Boolean isIndoor;
    private String pitchType;

    public static VenueResponse mapToResponse(Venue venue) {
        if (venue == null) {
            return null;
        }
        return VenueResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .city(venue.getCity())
                .country(venue.getCountry())
                .address(venue.getAddress())
                .capacity(venue.getCapacity())
                .isIndoor(venue.getIsIndoor())
                .pitchType(venue.getPitchType())
                .build();
    }
}
