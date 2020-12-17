package dev.kscott.quantum.location;

import com.google.inject.Inject;
import dev.kscott.quantum.rule.QuantumRule;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantum.rule.ruleset.search.SearchArea;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LocationProvider, the core of Quantum. Generates spawn points, handles spawn rules, etc
 */
public class LocationProvider {

    /**
     * CachedThreadPool executor
     */
    private final @NonNull ExecutorService executor;

    /**
     * Random reference
     */
    private final @NonNull Random random;

    @Inject
    public LocationProvider() {
        this.executor = Executors.newCachedThreadPool();
        this.random = new Random();
    }

    /**
     * Returns a random spawn location for {@code world}
     * @param ruleset The ruleset to search this world for
     * @return A CompletableFuture<Location>. Will complete when a valid location is found.
     */
    public CompletableFuture<Location> getSpawnLocation(final @NonNull QuantumRuleset ruleset) {
        final @Nullable World world = Bukkit.getWorld(ruleset.getWorldUuid());


        if (world == null) {
            throw new RuntimeException("World must not be null! Please ensure your world name was correct in quantum.conf.");
        }

        final @NonNull SearchArea searchArea = ruleset.getSearchArea();

        final int x = random.nextInt((searchArea.getMaxX() - searchArea.getMinX()) + 1) - searchArea.getMinX();
        final int z = random.nextInt((searchArea.getMaxZ() - searchArea.getMinZ()) + 1) - searchArea.getMinZ();

        final int chunkX = x >> 4;
        final int chunkZ = z >> 4;

        return world.getChunkAtAsync(chunkX, chunkZ)
                .thenApply(Chunk::getChunkSnapshot)
                .thenApplyAsync(snapshot -> {
                    final int relativeX = x & 0b1111;
                    final int relativeZ = z & 0b1111;

                    final int y = snapshot.getHighestBlockYAt(relativeX, relativeZ);

                    boolean valid = true;

                    for (QuantumRule rule : ruleset.getRules()) {
                        System.out.println(rule.getOptions());
                        if (!rule.validate(snapshot, relativeX, y, relativeZ)) {
                            valid = false;
                            break;
                        }
                    }

                    if (valid) {
                        return new Location(world, x, y, z);
                    } else {
                        return getSpawnLocation(ruleset).join();
                    }
                }, executor);
    }
}
