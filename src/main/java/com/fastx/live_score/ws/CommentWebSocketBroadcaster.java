package com.fastx.live_score.ws;

import com.fastx.live_score.events.MatchUpdatedEvent;
import com.fastx.live_score.modules.liveMatch.response.MatchDto;
import com.fastx.live_score.modules.liveMatch.response.MatchDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.MatchControl;
import org.example.events.MatchEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentWebSocketBroadcaster {

    private static final String SCORECARD_TOPIC_PREFIX = "/topic/commentary/";

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleMatchUpdate(@NotNull MatchUpdatedEvent event) {
        MatchControl matchControl = event.getMatchControl();
        MatchEvent lastEvent = matchControl.event();
        String destination = SCORECARD_TOPIC_PREFIX + event.getMatchId();
        messagingTemplate.convertAndSend(destination, lastEvent);
    }

}
