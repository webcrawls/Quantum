package dev.kscott.quantum.location;

import com.google.inject.Inject;
import dev.kscott.quantum.world.QuantumWorld;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LocationProvider, the core of Quantum. Generates spawn points, handles spawn rules, etc
 */
public class LocationProvider {

    /**
     * JavaPlugin reference
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * CachedThreadPool executor
     */
    private final @NonNull ExecutorService executor;

    private final @NonNull Random random;

    @Inject
    public LocationProvider(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.executor = Executors.newCachedThreadPool();
        this.random = new Random();
    }

    /**
     * Returns a random spawn location for {@code world}
     * @param quantumWorld The world that applies to
     * @return
     */
    public CompletableFuture<Location> getSpawnLocation(final @NonNull QuantumWorld quantumWorld) {
        final World world = Bukkit.getWorld(quantumWorld.getWorldUuid()); // TODO: Make this a method argument

        final int x = random.nextInt(10000) - 500;
        final int z = random.nextInt(10000 - 500);

        System.out.println(Thread.currentThread().getName());

        System.out.println("Testing x" + x + ", " + z);

        final int chunkX = x >> 4;
        final int chunkZ = z >> 4;

        return world.getChunkAtAsync(chunkX, chunkZ)
                .thenApply(chunk -> {
                    System.out.println(Thread.currentThread().getName());
                    return chunk.getChunkSnapshot();
                })
                .thenApplyAsync(snapshot -> {
                    System.out.println(Thread.currentThread().getName());

                    final int relativeX = x & 0b1111;
                    final int relativeZ = z & 0b1111;

                    final int y = snapshot.getHighestBlockYAt(relativeX, relativeZ);

                    System.out.println("Validating x" + x + ", y" + y + ", z" + z);
                    boolean valid = validateLocation(snapshot, relativeX, y, relativeZ);

                    if (valid) {
                        System.out.println("Is valid");
                        return new Location(world, x, y, z);
                    } else {
                        return getSpawnLocation().join();
                    }
                }, executor);
    }

    /**
     * Validates the location, and returns a boolean.
     *
     * @param snapshot ChunkSnapshot of the location
     * @param x        X of the location (relative to chunk, 0-15)
     * @param y        Y of the location (0-255)
     * @param z        Z of the location (relative to chunk, 0-15)
     * @return true if location is safe, false if not
     */
    private boolean validateLocation(final @NonNull ChunkSnapshot snapshot, final int x, final int y, final int z) {
        final @NonNull BlockData blockData = snapshot.getBlockData(x, y, z);

        System.out.println(blockData.getMaterial().name());

        if (blockData.getMaterial() == Material.WATER) {
            this.plugin.getLogger().info("Invalid location, spawn is water");
            return false;
        }

        return true;
    }

}
