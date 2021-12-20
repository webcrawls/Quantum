package sh.kaden.quantum.paper.world.chunk;

import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.world.chunk.Chunk;

public class PaperChunk implements Chunk {

    private final org.bukkit.@NonNull Chunk chunk;

    public PaperChunk(final org.bukkit.@NonNull Chunk chunk) {
        this.chunk = chunk;
    }

}
