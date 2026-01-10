package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * A very simple implementation of a player that just holds the player's id, name, and role.
 */
@Immutable
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SimplePlayer implements Player {

    private final Long id;
    private final String fullName;
    private final String role; // ✅ New field for role

    @JsonCreator
    public SimplePlayer(
            @JsonProperty("id") Long id,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("role") String role) {
        this.id = id;
        this.fullName = fullName;
        this.role = role;
    }

    @Nonnull
    @Override
    public Long id() {
        return id;
    }


    @Nonnull
    @Override
    public String name() {
        return fullName;
    }

    public String role() {
        return role;
    }

    @Override
    public boolean samePlayer(@Nullable Player other) {
        if (other == null) {
            return false;
        }
        if (other instanceof SimplePlayer otherPlayer) {
            return Objects.equals(id, otherPlayer.id);
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return samePlayer((Player) obj);
    }

    @Override
    public String toString() {
        return fullName + " (" + role + ")";
    }
}
