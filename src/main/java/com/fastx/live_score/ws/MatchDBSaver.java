package com.fastx.live_score.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fastx.live_score.modules.liveMatch.entity.LiveMatchEventEntity;
import com.fastx.live_score.modules.match.db.MatchEntity;
import com.fastx.live_score.modules.liveMatch.entity.LiveMatchEventEntityRepository;
import com.fastx.live_score.modules.match.db.MatchEntityRepository;
import com.fastx.live_score.events.MatchUpdatedEvent;
import org.example.events.MatchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class MatchDBSaver {

    private final LiveMatchEventEntityRepository liveMatchEventEntityRepository;
    private final MatchEntityRepository matchEntityRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @EventListener
    public void handleMatchUpdate(@NotNull MatchUpdatedEvent event) {
        MatchEntity matchEntity = matchEntityRepository
                .findByLiveMatchId(UUID.fromString(event.getMatchId()))
                .orElseThrow();
        event.getMatchControl().history().forEach(matchControl -> {
            MatchEvent currentEvent = matchControl.event();
            try {
                String json = objectMapper.writeValueAsString(currentEvent);
                LiveMatchEventEntity liveMatchEventEntity = LiveMatchEventEntity.builder()
                        .matchEntity(matchEntity)
                        .eventData(json)
                        .id(currentEvent.id())
                        .eventName(currentEvent.getClass().getName())
                        .createdDate(currentEvent.time())
                        .lastModifiedDate(currentEvent.time())
                        .build();
                liveMatchEventEntityRepository.save(liveMatchEventEntity);
            } catch (Exception e) {
                log.error("Failed to save match event: {}", currentEvent, e);
            }
        });
    }
}
