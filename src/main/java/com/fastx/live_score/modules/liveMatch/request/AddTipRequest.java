package com.fastx.live_score.modules.liveMatch.request;

import lombok.Data;

import java.util.List;

@Data
public class AddTipRequest {
    private List<String> tipData;
}
