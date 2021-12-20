package sh.kaden.quantum.core.world;

import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.world.chunk.Chunk;
import sh.kaden.quantum.core.world.chunk.ChunkSnapshot;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * TODO javadoc
 */
public interface World {

    /**
     * Returns the name of the world.
     *
     * @return the name
     */
    @NonNull String name();

    /**
     * Returns the world's UUID.
     * @return the uuid
     */
    @NonNull UUID uuid();

    /**
     * Returns a new {@link CompletableFuture} that is completed
     * when the chunk at {@code x}, {@code z} is loaded from disk or memory.
     * @param x the x chunk coordinate
     * @param z the z chunk coordinate
     * @return a completable future
     */
    @NonNull CompletableFuture<Chunk> chunkAt(final int x,
                                              final int z);

    /**
     * Returns a new {@link CompletableFuture} that is completed
     * when the {@link Chunk} is available, and a snapshot is
     * created.
     * @param x the x chunk coordinate
     * @param z the z chunk coordinate
     * @return a completable future
     */
    @NonNull CompletableFuture<ChunkSnapshot> chunkSnapshotAt(final int x,
                                                              final int z);

}
