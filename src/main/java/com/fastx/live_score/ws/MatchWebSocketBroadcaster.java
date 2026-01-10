package com.fastx.live_score.ws;

import com.fastx.live_score.events.MatchUpdatedEvent;
import com.fastx.live_score.util.MatchUtil;
import com.fastx.live_score.modules.liveMatch.response.BallCompletedEventDto;
import org.example.events.BallCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class MatchWebSocketBroadcaster {

    private static final String TOPIC_PREFIX = "/topic/match/";

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleMatchUpdate(@NotNull MatchUpdatedEvent event) {
        List<BallCompletedEvent> lastN = MatchUtil.getLastNBallEvents(event.getMatchControl(), 24);
        List<BallCompletedEventDto> recentBalls = lastN.stream()
                .map(event1 -> BallCompletedEventDto.toDto(event1, event.getMatchControl()))
                .toList();

        if (recentBalls.isEmpty()) {
            log.debug("No ball events to broadcast for match {}", event.getMatchId());
            return;
        }
        String destination = TOPIC_PREFIX + event.getMatchId();
        messagingTemplate.convertAndSend(destination, recentBalls);
        log.info("Broadcasted {} ball events to {}", recentBalls.size(), destination);
    }
}
