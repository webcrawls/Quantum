package dev.kscott.quantum.rule.ruleset.target;

/**
 * Tells Quantum to search for spawns between minY and maxY
 */
public class RangeSpawnTarget implements SpawnTarget {

    private final int minY;

    private final int maxY;

    public RangeSpawnTarget(final int minY, final int maxY) {
        this.minY = minY;
        this.maxY = maxY;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

}
