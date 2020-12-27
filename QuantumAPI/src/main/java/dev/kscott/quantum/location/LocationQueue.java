package dev.kscott.quantum.location;

import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * The LocationQueue class. Handles Location queues for every
 * QuantumRuleset.
 */
public class LocationQueue {

    /**
     * A map where the key is a  QuantumRuleset,
     * and the value is a set of locations to be used as the location queue.
     */
    private final @NonNull Map<QuantumRuleset, ConcurrentLinkedQueue<QuantumLocation>> locationQueueMap;

    /**
     *
     */
    private final @NonNull LocationProvider locationProvider;

    private final @NonNull RulesetRegistry rulesetRegistry;

    /**
     * Constructs the LocationQueue
     */
    public LocationQueue(
            final @NonNull LocationProvider locationProvider,
            final @NonNull RulesetRegistry rulesetRegistry
    ) {
        this.locationQueueMap = new HashMap<>();
        this.locationProvider = locationProvider;
        this.rulesetRegistry = rulesetRegistry;

        for (final QuantumRuleset ruleset : rulesetRegistry.getRulesets()) {
            if (ruleset.getQueueTarget() == 0) {
                continue;
            }

            getLocations(ruleset);
        }
    }

    protected void pushLocation(final @NonNull QuantumRuleset ruleset, final @NonNull QuantumLocation location) {
        final @Nullable Queue<QuantumLocation> locations = locationQueueMap.computeIfAbsent(ruleset, k -> new ConcurrentLinkedQueue<>());

        locations.add(location);
    }

    protected int getLocationCount(final @NonNull QuantumRuleset ruleset) {
        final @Nullable Queue<QuantumLocation> locations = locationQueueMap.get(ruleset);

        return locations == null ? 0 : locations.size();
    }

    protected @Nullable QuantumLocation popLocation(final @NonNull QuantumRuleset ruleset) {
        final @Nullable Queue<QuantumLocation> locations = locationQueueMap.get(ruleset);

        if (locations == null || locations.isEmpty()) {
            return null;
        }

        final @NonNull QuantumLocation location = locations.iterator().next();

        locations.remove(location);

        return location;
    }

    protected void getLocations(final @NonNull QuantumRuleset ruleset) {
        final int target = ruleset.getQueueTarget();

        for (int i = 0; i < target; i++) {
            final int index = i;
            System.out.println("queuing "+i+" for "+ruleset.getId());
            final @NonNull CompletableFuture<QuantumLocation> cf = new CompletableFuture<>();

            this.locationProvider.findLocation(0, System.currentTimeMillis(), ruleset, cf);

            cf.thenAccept(quantumLocation -> {
                final @Nullable Queue<QuantumLocation> locations = locationQueueMap.computeIfAbsent(ruleset, k -> new ConcurrentLinkedQueue<>());

                System.out.println("queue "+index+"done for "+ruleset.getId());

                locations.add(quantumLocation);
            });
        }

        System.out.println("get done");
    }
}
