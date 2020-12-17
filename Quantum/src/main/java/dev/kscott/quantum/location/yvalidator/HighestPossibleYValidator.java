package dev.kscott.quantum.location.yvalidator;

import org.bukkit.ChunkSnapshot;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A YValidator that returns the highest Y
 */
public class HighestPossibleYValidator implements YValidator {

    /**
     * Checks against the snapshot's heightmap to get the highest Y value
     * @param snapshot The snapshot of the chunk to look for
     * @param x the X coordinate (relative to chunk, 0-15)
     * @param z the Z coordinate (relative to chunk, 0-15)
     * @return The highest valid Y value
     */
    @Override
    public int getValidY(@NonNull ChunkSnapshot snapshot, int x, int z) {
        return snapshot.getHighestBlockYAt(x, z);
    }
}
