package com.fastx.live_score.modules.venue.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class VenueRequest {

    @CsvBindByName(column = "Name", required = true)
    private String name;

    @CsvBindByName(column = "City", required = true)
    private String city;

    @CsvBindByName(column = "Country", required = true)
    private String country;

    @CsvBindByName(column = "Address")
    private String address;

    @CsvBindByName(column = "Capacity")
    private Integer capacity;

    @CsvBindByName(column = "IsIndoor")
    private Boolean isIndoor;

    @CsvBindByName(column = "PitchType")
    private String pitchType;
}
