package com.fastx.live_score.core.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fastx.live_score.events.MatchUpdatedEvent;
import com.fastx.live_score.modules.liveMatch.response.BallCompletedEventDto;
import com.fastx.live_score.modules.liveMatch.response.OverEndingSummary;
import org.example.Match;
import org.example.MatchControl;
import org.example.events.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class LiveMatchFileRepository {

    private static final Path STORAGE_DIR = Path.of("match-events");
    private static final TypeReference<List<BaseMatchEvent>> MATCH_EVENT_LIST_TYPE = new TypeReference<>() {
    };
    private final Map<String, MatchControl> matches = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final ApplicationEventPublisher publisher;

    static {
        File dir = STORAGE_DIR.toFile();
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Failed to create match-events directory");
        }
    }

    /**
     * Retrieves MatchControl from in-memory cache or loads from file.
     */
    public MatchControl findById(String matchId) {
        return matches.computeIfAbsent(matchId, this::loadFromFile);
    }

    /**
     * Retrieves all events (from memory if available, otherwise from file).
     */
    public Object getAllEvents(String matchId) {
        MatchControl control = matches.get(matchId);
        if (control != null) {
            MatchControl finalControl1 = control;
            return control.history().stream().map(MatchControl::event).map(baseMatchEvent -> {
                if (baseMatchEvent instanceof BallCompletedEvent) {
                    return BallCompletedEventDto.toDto((BallCompletedEvent) baseMatchEvent, finalControl1);
                } else if (baseMatchEvent instanceof OverCompletedEvent) {
                    MatchControl at = finalControl1.asAt(baseMatchEvent);
                    return OverEndingSummary.fromMatch(at.match());
                } else return baseMatchEvent;
            }).toList();
        }
        control = loadFromFile(matchId);
        File file = getFileForMatch(matchId);
        if (!file.exists()) {
            throw new IllegalArgumentException("No saved match events found for matchId: " + matchId);
        }
        try {
            MatchControl finalControl = control;
            return objectMapper.readValue(file, MATCH_EVENT_LIST_TYPE).stream().map(baseMatchEvent -> {
                if (baseMatchEvent instanceof BallCompletedEvent) {
                    return BallCompletedEventDto.toDto((BallCompletedEvent) baseMatchEvent,finalControl);
                } else if (baseMatchEvent instanceof OverCompletedEvent) {
                    MatchControl at = finalControl.asAt(baseMatchEvent);
                    return OverEndingSummary.fromMatch(at.match());
                } else return baseMatchEvent;
            }).toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load events for matchId=" + matchId, e);
        }
    }


    /**
     * Saves the MatchControl state to memory and file.
     */
    public void save(String matchId, MatchControl control) {
        matches.put(matchId, control);
        publisher.publishEvent(new MatchUpdatedEvent(this, matchId, control));
        try {
            persistMatchToFile(matchId, control);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save match events to file for matchId=" + matchId, e);
        }
        if (control.match().state() == Match.State.COMPLETED) {
            matches.remove(matchId);
        }
    }

    /**
     * Returns all currently live matches (in memory only).
     */
    public Collection<MatchControl> getLiveMatches() {
        return Collections.unmodifiableCollection(matches.values());
    }

    // ------------------- PRIVATE METHODS ------------------- //

    private void persistMatchToFile(String matchId, MatchControl control) throws IOException {
        List<MatchEvent> allEvents = control.history().stream().map(MatchControl::event).toList();

        File file = getFileForMatch(matchId);
        objectMapper.writerFor(MATCH_EVENT_LIST_TYPE).writeValue(file, allEvents);
    }

    private MatchControl loadFromFile(String matchId) {
        File file = getFileForMatch(matchId);
        if (!file.exists()) {
            throw new IllegalArgumentException("No saved match events found for matchId: " + matchId);
        }

        try {
            List<BaseMatchEvent> events = objectMapper.readValue(file, MATCH_EVENT_LIST_TYPE);
            if (events.isEmpty()) {
                throw new IllegalStateException("No events found in file for matchId: " + matchId);
            }

            BaseMatchEvent first = events.get(0);
            if (!(first instanceof MatchStartingEvent startingEvent)) {
                throw new IllegalStateException("First event must be a MatchStartingEvent");
            }

            MatchControl control = MatchControl.newMatch(startingEvent);
            for (int i = 1; i < events.size(); i++) {
                control = control.onEvent(events.get(i));
            }

            return control;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load match from file for matchId=" + matchId, e);
        } catch (ClassCastException | IllegalStateException e) {
            throw new RuntimeException("Corrupted or invalid match data for matchId=" + matchId, e);
        }
    }

    private File getFileForMatch(String matchId) {
        return STORAGE_DIR.resolve(matchId + ".json").toFile();
    }
}
