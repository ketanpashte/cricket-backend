package com.fastx.live_score.modules.liveMatch.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListPlayerDto {
    private String name;
    private boolean isCaptain;
    private boolean isWicketKeeper;
}
