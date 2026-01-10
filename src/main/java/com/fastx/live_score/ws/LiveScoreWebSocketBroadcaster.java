package com.fastx.live_score.ws;

import com.fastx.live_score.events.MatchUpdatedEvent;
import com.fastx.live_score.modules.liveMatch.response.LiveScore;
import org.example.MatchControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiveScoreWebSocketBroadcaster {

    private static final String SCORE_TOPIC_PREFIX = "/topic/live-score/";

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleMatchUpdate(@NotNull MatchUpdatedEvent event) {
        log.info("Received MatchUpdateEvent for " + event.getMatchId());
        MatchControl matchControl = event.getMatchControl();
        LiveScore liveScore = LiveScore.fromMatchControl(matchControl);
        String destination = SCORE_TOPIC_PREFIX + event.getMatchId();
        messagingTemplate.convertAndSend(destination, liveScore);
        log.info("Broadcasted live score to {}", destination);
    }

}
