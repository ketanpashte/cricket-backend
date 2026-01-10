package com.fastx.live_score.modules.liveMatch.request;

import org.example.DismissalType;
import lombok.Data;

@Data
public class DismissalRequest {
    private String outcome;
    private DismissalType dismissalType;
    private Long fielderId;
}
