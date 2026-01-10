package org.example.events;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;

public interface MatchEvent {
    @Nonnull
    UUID id();

    @Nullable
    Instant time();

    @Nonnull
    MatchEventBuilder<?, ?> newBuilder();

    @Nullable
    Object customData();

    boolean undoPoint();
}
