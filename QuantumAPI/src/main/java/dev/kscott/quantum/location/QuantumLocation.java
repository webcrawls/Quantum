package dev.kscott.quantum.location;

import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a completed Quantum location request.
 */
public class QuantumLocation {

    /**
     * Clones {@code location} and returns the clone with all values set to 0.5
     *
     * @param location Location to center
     * @return Centered location
     */
    public static @NonNull Location toCenterLocation(final @NonNull Location location) {
        Location centerLoc = location.clone();
        centerLoc.setX(location.getBlockX() + 0.5);
        centerLoc.setY(location.getBlockY() + 0.5);
        centerLoc.setZ(location.getBlockZ() + 0.5);
        return centerLoc;
    }

    /**
     * Clones {@code location} and returns the clone with x/z values set to 0.5, and y value to 0.
     *
     * @param location Location to center
     * @return Centered location
     */
    public static @NonNull Location toCenterHorizontalLocation(final @NonNull Location location) {
        Location centerLoc = location.clone();
        centerLoc.setX(location.getBlockX() + 0.5);
        centerLoc.setY(location.getBlockY());
        centerLoc.setZ(location.getBlockZ() + 0.5);
        return centerLoc;
    }

    /**
     * The amount of time (in milliseconds) it took to generate this location.
     */
    private final long duration;

    /**
     * The generated location.
     */
    private final @NonNull Location location;

    /**
     * The ruleset used to generate this location.
     */
    private final @NonNull QuantumRuleset ruleset;

    /**
     * The QuantumLocation ctor
     *
     * @param duration {@link this#duration}
     * @param location {@link this#location}
     * @param ruleset  {@link this#ruleset}
     */
    public QuantumLocation(
            final long duration,
            final @NonNull Location location,
            final @NonNull QuantumRuleset ruleset
    ) {
        this.duration = duration;
        this.location = location;
        this.ruleset = ruleset;
    }

    /**
     * @return {@link this#duration}
     */
    public long getDuration() {
        return duration;
    }

    /**
     * @return {@link this#location}
     */
    public @NonNull Location getLocation() {
        return location;
    }

    /**
     * @return {@link this#ruleset}
     */
    public @NonNull QuantumRuleset getRuleset() {
        return ruleset;
    }
}
