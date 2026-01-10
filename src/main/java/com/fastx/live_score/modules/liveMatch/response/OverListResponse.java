package com.fastx.live_score.modules.liveMatch.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverListResponse {
    Long teamId;
    int inningsNo;
    String teamName;
    List<MatchDto.OverDto> overs;
}
