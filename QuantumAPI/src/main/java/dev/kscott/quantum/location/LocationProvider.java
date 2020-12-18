package dev.kscott.quantum.location;

import cloud.commandframework.paper.PaperCommandManager;
import dev.kscott.quantum.config.Config;
import dev.kscott.quantum.rule.rules.async.AsyncQuantumRule;
import dev.kscott.quantum.rule.rules.sync.SyncQuantumRule;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantum.rule.ruleset.search.SearchArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * LocationProvider, the core of Quantum. Generates spawn points, handles spawn rules, etc
 */
public class LocationProvider {

    /**
     * Random reference
     */
    private final @NonNull Random random;

    /**
     * PaperCommandManager reference
     */
    private final @NonNull PaperCommandManager<CommandSender> commandManager;

    /**
     * QuantumTimer reference
     */
    private final @NonNull QuantumTimer timer;

    /**
     * Config reference
     */
    private final @NonNull Config config;


    /**
     * Constructs the LocationProvider
     *
     * @param config         {@link this#config}
     * @param timer          {@link this#timer}
     * @param commandManager {@link this#commandManager}
     */
    public LocationProvider(
            final @NonNull Config config,
            final @NonNull QuantumTimer timer,
            final @NonNull PaperCommandManager<CommandSender> commandManager
    ) {
        this.commandManager = commandManager;
        this.random = new Random();
        this.timer = timer;
        this.config = config;
    }

    /**
     * Returns a random spawn location for {@code world}
     *
     * @param ruleset The ruleset to use for this search
     * @return A CompletableFuture<QuantumLocation>. Will complete when a valid location is found (or max retries is hit).
     * If max retries is hit, {@link QuantumLocation#getLocation()} will return {@code null} and {@link QuantumLocation#isSuccess()} will return {@code false}.
     */
    public @NonNull CompletableFuture<QuantumLocation> getSpawnLocation(final @NonNull QuantumRuleset ruleset) {
        return this.getSpawnLocation(0, System.currentTimeMillis(), ruleset);
    }

    /**
     * Returns a random spawn location for {@code world}
     *
     * @param quantumRuleset The ruleset to use for this search
     * @param start          When this search was started
     * @return A CompletableFuture<Location>. Will complete when a valid location is found.
     */
    private @NonNull CompletableFuture<QuantumLocation> getSpawnLocation(final int tries, final long start, final @NonNull QuantumRuleset quantumRuleset) {

        final @NonNull CompletableFuture<QuantumLocation> cf = new CompletableFuture<>();

        if (this.config.getMaxRetries() <= tries) {
            cf.complete(new QuantumLocation(0, false, null, quantumRuleset));
            return cf;
        }

        this.commandManager.taskRecipe()
                .begin(quantumRuleset)
                .synchronous(ruleset -> {
                    // Get the world and constuct the QuantumState
                    final @Nullable World world = Bukkit.getWorld(ruleset.getWorldUuid());

                    if (world == null) {
                        throw new RuntimeException("World cannot be null!");
                    }

                    final @NonNull QuantumLocationState state = new QuantumLocationState();

                    state.setWorld(world);
                    state.setQuantumRuleset(quantumRuleset);

                    return state;
                })
                .asynchronous(state -> {
                    // Do some random generation
                    final @NonNull SearchArea searchArea = state.getQuantumRuleset().getSearchArea();

                    final int x = random.nextInt((searchArea.getMaxX() - searchArea.getMinX()) + 1) + searchArea.getMinX();
                    final int z = random.nextInt((searchArea.getMaxZ() - searchArea.getMinZ()) + 1) + searchArea.getMinZ();

                    state.setX(x);
                    state.setZ(z);

                    return state;
                })
                .synchronous(state -> {
                    // Get the chunk future and add it to the state
                    state.setChunkFuture(state.getWorld().getChunkAtAsync(state.getChunkX(), state.getChunkZ()));

                    return state;
                })
                .asynchronous(state -> {
                    // Get the chunk from the future
                    try {
                        state.setChunk(state.getChunkFuture().get());
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException("Error getting cf");
                    }

                    return state;
                })
                .synchronous(state -> {
                    // get & set the snapshot
                    state.setSnapshot(state.getChunk().getChunkSnapshot());

                    return state;
                })
                .asynchronous(state -> {
                    // Get the y value and check async rules

                    state.setValid(true);

                    final int y = state.getQuantumRuleset().getYLocator().getValidY(state.getSnapshot(), state.getRelativeX(), state.getRelativeZ());


                    if (y == -1) {
                        state.setValid(false);
                        return state;
                    }

                    state.setY(y);

                    for (final AsyncQuantumRule rule : state.getQuantumRuleset().getAsyncRules()) {
                        boolean valid = rule.validate(state.getSnapshot(), state.getRelativeX(), y, state.getRelativeZ());

                        state.setValid(valid);

                        if (!valid) {
                            break;
                        }
                    }

                    return state;
                })
                .synchronous(state -> {
                    // Check sync rules
                    for (final SyncQuantumRule rule : state.getQuantumRuleset().getSyncRules()) {
                        boolean valid = rule.validate(state.getChunk(), state.getRelativeX(), state.getY(), state.getRelativeZ());

                        state.setValid(valid);

                        if (!valid) {
                            break;
                        }
                    }

                    return state;
                })
                .asynchronous(state -> {
                    // Complete the cf, either with the new location, or the results of a recursive getSpawnLocation call
                    if (state.isValid()) {
                        final long searchTime = System.currentTimeMillis() - start;

                        cf.complete(new QuantumLocation(
                                searchTime,
                                true,
                                new Location(state.getWorld(), state.getX(), state.getY(), state.getZ()),
                                state.getQuantumRuleset()
                        ));

                        this.timer.addTime(searchTime);
                    } else {
                        cf.complete(getSpawnLocation(tries+1, start, state.getQuantumRuleset()).join());
                    }
                })
                .execute();

        return cf;
    }
}
