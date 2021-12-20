package sh.kaden.quantum.core.location;

import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.paper.world.World;

/**
 * A location representing a single, immutable location in a {@link World}.
 */
public class BlockVector {

    /**
     * Returns a new {@link BlockVector} instance with the provided values.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the location
     */
    public static @NonNull BlockVector of(final double x,
                                          final double y,
                                          final double z) {
        return new BlockVector(x, y, z);
    }

    private final double x;
    private final double y;
    private final double z;

    public BlockVector(final double x,
                       final double y,
                       final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return this.x;
    }

    public double centerX() {
        return this.blockX() + 0.5;
    }

    public int blockX() {
        return (int) this.x;
    }

    public double y() {
        return this.y;
    }

    public double centerY() {
        return this.blockY() + 0.5;
    }

    public int blockY() {
        return (int) this.y;
    }

    public double z() {
        return this.z;
    }

    public double centerZ() {
        return this.blockZ() + 0.5;
    }

    public int blockZ() {
        return (int) this.z;
    }

}
