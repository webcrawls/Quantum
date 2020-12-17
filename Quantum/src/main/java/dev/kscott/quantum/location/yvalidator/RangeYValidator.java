package dev.kscott.quantum.location.yvalidator;

import org.bukkit.ChunkSnapshot;
import org.checkerframework.checker.nullness.qual.NonNull;

public class RangeYValidator implements YValidator {

    /**
     * The minimum Y to start checking from
     */
    private final int minY;

    /**
     * The maximum Y to stop checking at
     */
    private final int maxY;

    /**
     * Constructs RangeYValidator
     * @param minY {@link this#minY}
     * @param maxY {@link this#maxY}
     */
    public RangeYValidator(final int minY, final int maxY) {
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     *
     * @param snapshot The snapshot of the chunk to look for
     * @param x the X coordinate (relative to chunk, 0-15)
     * @param z the Z coordinate (relative to chunk, 0-15)
     * @return the lowest valid Y value between {@link this#minY} and {@link this#maxY}
     */
    @Override
    public int getValidY(@NonNull ChunkSnapshot snapshot, int x, int z) {
        for (int y = minY; y <= maxY; y++) {
            final boolean yClear = snapshot.getBlockType(x, y, z).isAir();
            final boolean yAboveClear = snapshot.getBlockType(x, y+1, z).isAir();

            if (yClear && yAboveClear) {
                return y;
            }
        }

        return -1;
    }
}
