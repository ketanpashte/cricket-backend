package com.fastx.live_score.modules.team;

import com.fastx.live_score.modules.player.dto.ListPlayerRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Team {
    private Long id;
    private String name;
    private String shortCode;
    private String logoUrl;
    private String coach;
    private List<ListPlayerRes> players;
    private String captainName;
    private Long captainId;
    private String colorCode;
    private Long viceCaptainId;
    private String bgImage;

}
