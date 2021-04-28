package dev.kscott.quantum.location;

import com.google.common.collect.ImmutableMap;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Handles {@link QuantumLocation} queues for every {@link QuantumRuleset}.
 */
public class LocationQueue {

    /**
     * A map where the key is a {@link QuantumRuleset},
     * and the value is a set of locations to be used as the location queue.
     */
    private final @NonNull Map<QuantumRuleset, ConcurrentLinkedQueue<QuantumLocation>> locationQueueMap;

    /**
     * {@link LocationProvider} reference.
     */
    private final @NonNull LocationProvider locationProvider;

    /**
     * Constructs {@link LocationQueue}.
     *
     * @param locationProvider {@link LocationProvider} reference.
     * @param rulesetRegistry {@link RulesetRegistry} reference.
     */
    public LocationQueue(
            final @NonNull LocationProvider locationProvider,
            final @NonNull RulesetRegistry rulesetRegistry
    ) {
        this.locationQueueMap = new HashMap<>();
        this.locationProvider = locationProvider;

        for (final QuantumRuleset ruleset : rulesetRegistry.getRulesets()) {
            if (ruleset.getQueueTarget() == 0) {
                continue;
            }

            getLocations(ruleset);
        }
    }

    /**
     * Pushes a location to the top of the location queue.
     *
     * @param ruleset  Ruleset to associate the location with.
     * @param location Location to add.
     */
    protected void pushLocation(final @NonNull QuantumRuleset ruleset, final @NonNull QuantumLocation location) {
        final @Nullable Queue<QuantumLocation> locations = locationQueueMap.computeIfAbsent(ruleset, k -> new ConcurrentLinkedQueue<>());

        locations.add(location);
    }

    /**
     * Returns the amount of locations queued for a given ruleset.
     *
     * @param ruleset Ruleset to get location queue size for.
     * @return size of queue for {@code ruleset}.
     */
    protected int getLocationCount(final @NonNull QuantumRuleset ruleset) {
        final @Nullable Queue<QuantumLocation> locations = locationQueueMap.get(ruleset);

        return locations == null ? 0 : locations.size();
    }

    /**
     * Pops a Location from the queue, and returns it.
     *
     * @param ruleset Ruleset to get the Location of.
     * @return Location queued for {@code ruleset}.
     */
    protected @Nullable QuantumLocation popLocation(final @NonNull QuantumRuleset ruleset) {
        final @Nullable Queue<QuantumLocation> locations = locationQueueMap.get(ruleset);

        if (locations == null || locations.isEmpty()) {
            return null;
        }

        final @NonNull QuantumLocation location = locations.iterator().next();

        locations.remove(location);

        return location;
    }

    /**
     * Queues locations for a ruleset, based on the ruleset queue target.
     *
     * @param ruleset Ruleset to queue for.
     */
    protected void getLocations(final @NonNull QuantumRuleset ruleset) {
        final int target = ruleset.getQueueTarget() - this.getLocationCount(ruleset);

        for (int i = 0; i < target; i++) {
            final @NonNull CompletableFuture<QuantumLocation> cf = new CompletableFuture<>();

            this.locationProvider.findLocation(0, System.currentTimeMillis(), ruleset, cf);

            cf.thenAccept(quantumLocation -> {
                final @Nullable Queue<QuantumLocation> locations = locationQueueMap.computeIfAbsent(ruleset, k -> new ConcurrentLinkedQueue<>());

                locations.add(quantumLocation);
            });
        }
    }

    /**
     * Returns a Map of QuantumRuleset to Queue<QuantumLocation>. The queue holds all queued locations for the ruleset key.
     *
     * @return immutable map.
     */
    public @NonNull Map<QuantumRuleset, Queue<QuantumLocation>> getLocationMap() {
        return ImmutableMap.copyOf(this.locationQueueMap);
    }
}
