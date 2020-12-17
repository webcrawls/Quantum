package dev.kscott.quantum.location.yvalidator;

import org.bukkit.ChunkSnapshot;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The YValidator is used to locate Y values for a Quantum ruleset
 */
public interface YValidator {

    /**
     * Locates a valid Y value.
     * @param snapshot The snapshot of the chunk to look for
     * @param x the X coordinate (relative to chunk, 0-15)
     * @param z the Z coordinate (relative to chunk, 0-15)
     * @return a valid Y value
     */
    int getValidY(final @NonNull ChunkSnapshot snapshot, final int x, final int z);

}
