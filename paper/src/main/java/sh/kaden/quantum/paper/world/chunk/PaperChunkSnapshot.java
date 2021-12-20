package sh.kaden.quantum.paper.world.chunk;

import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.world.chunk.ChunkSnapshot;

public class PaperChunkSnapshot implements ChunkSnapshot {

    private final org.bukkit.@NonNull ChunkSnapshot snapshot;

    public PaperChunkSnapshot(final org.bukkit.@NonNull ChunkSnapshot snapshot) {
        this.snapshot = snapshot;
    }

}
