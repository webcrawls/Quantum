package dev.kscott.quantum.rule.rules.async;

import org.bukkit.ChunkSnapshot;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A rule which blocks locations which are air.
 */
public class AvoidAirRule extends AsyncQuantumRule {

    /**
     * Invalidates locations based on whether or not the block type is air.
     *
     * @param snapshot snapshot of the chunk
     * @param x        x coordinate, relative to chunk (0-15)
     * @param y        y coordinate (0-255)
     * @param z        z coordinate, relative to chunk (0-15)
     * @return true if block is not air, false if it is
     */
    @Override
    public boolean validate(@NonNull ChunkSnapshot snapshot, int x, int y, int z) {
        return !snapshot.getBlockData(x, y - 1, z).getMaterial().isAir();
    }
}
