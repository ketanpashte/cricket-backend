package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A very simple implementation of a line up that can be used instead of implementing your own {@link LineUp}
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SimpleLineUp implements LineUp<Player> {

    private final long id;
    private final ImmutableList<Player> players;
    private final Player captain;
    private final Player viceCaptain;
    private final Player wicketKeeper;
    private final String teamName;

    @JsonCreator
    public SimpleLineUp(
            @JsonProperty("id") long id, // ✅ Include in constructor
            @JsonProperty("players") ImmutableList<Player> players,
            @JsonProperty("captain") Player captain,
            @JsonProperty("viceCaptain") Player viceCaptain,
            @JsonProperty("wicketKeeper") Player wicketKeeper,
            @JsonProperty("teamName") String teamName
    ) {
        this.id = id;
        this.players = players;
        this.captain = captain;
        this.wicketKeeper = wicketKeeper;
        this.teamName = teamName;
        this.viceCaptain = viceCaptain;
    }

    public static Builder lineUp() {
        return new Builder();
    }

    public long getId() { // ✅ Getter for id
        return id;
    }

    @Nonnull
    @Override
    public ImmutableList<Player> battingOrder() {
        return players;
    }

    @Nullable
    @Override
    public Player captain() {
        return captain;
    }

    @Nullable
    @Override
    public Player wicketKeeper() {
        return wicketKeeper;
    }

    @Nonnull
    @Override
    public String teamName() {
        return teamName;
    }

    public Player viceCaptain() {
        return viceCaptain;
    }

    /**
     * Tries to find a player based on their name
     *
     * @param name The {@link Player#name()} value
     * @return The found player, or null if unsure
     */
    @Nullable
    public Player findPlayer(String name) {
        Player exact = battingOrder().stream().filter(p ->
                p.name().equalsIgnoreCase(name)
        ).findFirst().orElse(null);
        if (exact != null) {
            return exact;
        }
        String surname = name.split(" ")[name.split(" ").length - 1];
        List<Player> partials = battingOrder().stream().filter(p ->
                p.name().toLowerCase().endsWith(" " + surname.toLowerCase())
        ).toList();
        return partials.isEmpty() ? null : partials.get(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SimpleLineUp other = (SimpleLineUp) obj;
        return other.id == this.id;
    }

    @Override
    public boolean sameTeam(@org.jetbrains.annotations.Nullable LineUp<?> other) {
        if (other == null) {
            return false;
        }
        if (other instanceof SimpleLineUp) {
            SimpleLineUp otherSimpleLineUp = (SimpleLineUp) other;
            return otherSimpleLineUp.getId() == this.getId();
        }
        return false;
    }


    public static class Builder {

        private long id; // ✅ Add id in Builder
        private ImmutableList<Player> battingOrder;
        private Player captain;
        private Player wicketKeeper;
        private String teamName;
        private Player viceCaptain;

        public Builder withId(long id) { // ✅ Builder method for id
            this.id = id;
            return this;
        }

        public Builder withBattingOrder(ImmutableList<Player> battingOrder) {
            this.battingOrder = battingOrder;
            return this;
        }

        public Builder withCaptain(Player captain) {
            this.captain = captain;
            return this;
        }

        public Builder withWicketKeeper(Player wicketKeeper) {
            this.wicketKeeper = wicketKeeper;
            return this;
        }

        public Builder withTeamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Builder withViceCaptain(Player viceCaptain) {
            this.viceCaptain = viceCaptain;
            return this;
        }

        public SimpleLineUp build() {
            return new SimpleLineUp(id, battingOrder, captain, viceCaptain, wicketKeeper, teamName);
        }
    }
}
