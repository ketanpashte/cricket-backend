package com.fastx.live_score.modules.player.dto;

import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRequest {


    @CsvBindByName(column = "fullName")
    @NotNull(message = "Full Name is required")
    private String fullName;

    @CsvBindByName(column = "shortName")
    @NotNull(message = "Short Name is required")
    private String shortName;

    @CsvBindByName(column = "nationality")
    @NotNull(message = "Nationality is required")
    private String nationality;

    @CsvBindByName(column = "battingStyle")
    private String battingStyle;

    @CsvBindByName(column = "bowlingStyle")
    private String bowlingStyle;

    @CsvBindByName(column = "role")
    private String role;

}
