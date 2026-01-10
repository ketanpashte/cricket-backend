package com.fastx.live_score.ws;

import com.fastx.live_score.events.MatchUpdatedEvent;
import com.fastx.live_score.modules.liveMatch.response.LiveLineScore;
import com.fastx.live_score.modules.liveMatch.response.LiveScore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.MatchControl;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiveLineScoreWebSocketBroadcaster {

    private static final String SCORE_TOPIC_PREFIX = "/topic/live-line-score/";

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleMatchUpdate(@NotNull MatchUpdatedEvent event) {
        MatchControl matchControl = event.getMatchControl();
        LiveLineScore liveScore = LiveLineScore.getFromMatch(matchControl);
        String destination = SCORE_TOPIC_PREFIX + event.getMatchId();
        if (liveScore != null) {
            messagingTemplate.convertAndSend(destination, liveScore);
        }
    }

}
