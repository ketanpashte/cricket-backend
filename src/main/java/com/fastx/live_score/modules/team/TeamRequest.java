package com.fastx.live_score.modules.team;

import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {

    @NotNull
    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "logoUrl")
    private String logoUrl;

    @NotNull
    @CsvBindByName(column = "coach")
    private String coach;

    @NotNull
    @CsvBindByName(column = "shortCode")
    private String shortCode;

    @CsvBindByName(column = "captainId")
    private Long captainId;

    private Long viceCaptainId;

    private String colorCode;
    private String bgImage;


    private List<Long> players = new ArrayList<>();


}
