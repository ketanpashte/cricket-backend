package com.fastx.live_score.events;

import org.example.MatchControl;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MatchUpdatedEvent extends ApplicationEvent {
    private final String matchId;
    private final MatchControl matchControl;

    public MatchUpdatedEvent(Object source, String matchId, MatchControl matchControl) {
        super(source);
        this.matchId = matchId;
        this.matchControl = matchControl;
    }


}
