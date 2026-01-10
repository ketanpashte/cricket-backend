package com.fastx.live_score.modules.match.dto;

import lombok.Getter;

@Getter
public enum MatchStatus {

    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    PAUSED,
    CANCELLED;
}
