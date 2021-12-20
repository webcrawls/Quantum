package sh.kaden.quantum.paper.world;

import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.world.World;
import sh.kaden.quantum.core.world.chunk.Chunk;
import sh.kaden.quantum.core.world.chunk.ChunkSnapshot;
import sh.kaden.quantum.paper.world.chunk.PaperChunk;
import sh.kaden.quantum.paper.world.chunk.PaperChunkSnapshot;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Paper's {@link World} implementation using a Bukkit {@link org.bukkit.World}.
 */
public class PaperWorld implements World {

    private final org.bukkit.@NonNull World world;

    /**
     * Constructs {@code PaperWorld}.
     *
     * @param world the bukkit world
     */
    public PaperWorld(final org.bukkit.@NonNull World world) {
        this.world = world;
    }

    @Override
    public @NonNull String name() {
        return this.world.getName();
    }

    @Override
    public @NonNull UUID uuid() {
        return this.world.getUID();
    }

    @Override
    public @NonNull CompletableFuture<Chunk> chunkAt(final int x,
                                                     final int z) {
        return this.world.getChunkAtAsync(x, z)
                .thenApply(PaperChunk::new);
    }

    @Override
    public @NonNull CompletableFuture<ChunkSnapshot> chunkSnapshotAt(final int x,
                                                                     final int z) {
        return this.world.getChunkAtAsync(x, z)
                .thenApply(c -> new PaperChunkSnapshot(c.getChunkSnapshot(true, true, false)));
    }
}
