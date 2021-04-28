package dev.kscott.quantum.location.locator;

import org.bukkit.ChunkSnapshot;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A YLocator that searches between a min Y and a max Y.
 */
public class RangeYLocator implements YLocator {

    /**
     * The minimum Y to start checking from.
     */
    private final int minY;

    /**
     * The maximum Y to stop checking at.
     */
    private final int maxY;

    /**
     * Constructs RangeYValidator.
     *
     * @param minY {@link this#minY}.
     * @param maxY {@link this#maxY}.
     */
    public RangeYLocator(final int minY, final int maxY) {
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     * @param snapshot The snapshot of the chunk to look for.
     * @param x        the X coordinate (relative to chunk, 0-15).
     * @param z        the Z coordinate (relative to chunk, 0-15).
     * @return the lowest valid Y value between {@link this#minY} and {@link this#maxY}.
     */
    @Override
    public int getValidY(@NonNull ChunkSnapshot snapshot, int x, int z) {
        // TODO Change hard bound limits for 1.17+
        if (maxY > minY) {
            for (int y = minY; y <= maxY; y++) {
                boolean yBelowSafe;

                if (y - 1 < 0) {
                    yBelowSafe = false;
                } else {
                    yBelowSafe = snapshot.getBlockType(x, y - 1, z).isSolid();
                }

                final boolean yClear = snapshot.getBlockType(x, y, z).isAir();

                boolean yAboveClear;

                if (y + 1 > 255) {
                    yAboveClear = true;
                } else {
                    yAboveClear = snapshot.getBlockType(x, y + 1, z).isAir();
                }

                if (yClear && yAboveClear && yBelowSafe) {
                    return y;
                }
            }
        } else {
            for (int y = minY; y >= maxY; y--) {
                boolean yBelowSafe;

                if (y - 1 < 0) {
                    yBelowSafe = false;
                } else {
                    yBelowSafe = snapshot.getBlockType(x, y - 1, z).isSolid();
                }

                final boolean yClear = snapshot.getBlockType(x, y, z).isAir();

                boolean yAboveClear;

                if (y + 1 > 255) {
                    yAboveClear = true;
                } else {
                    yAboveClear = snapshot.getBlockType(x, y + 1, z).isAir();
                }

                if (yBelowSafe && yClear && yAboveClear) {
                    return y;
                }
            }
        }

        return -1;


    }
}
