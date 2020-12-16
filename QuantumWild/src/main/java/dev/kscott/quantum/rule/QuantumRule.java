package dev.kscott.quantum.rule;

import dev.kscott.quantum.rule.option.QuantumRuleOption;
import org.bukkit.ChunkSnapshot;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public abstract class QuantumRule {

    private final @NonNull String id;

    private final @NonNull List<QuantumRuleOption> options;

    public QuantumRule(
            final @NonNull String id,
            final @NonNull List<QuantumRuleOption> options) {
        this.id = id;
        this.options = options;
    }

    /**
     * Checks the location against this rule and returns it's validity as a boolean.
     * @param x x coordinate, relative to chunk (0-15)
     * @param y y coordinate (0-255)
     * @param z z coordinate, relative to chunk (0-15)
     * @return true if valid, false if not
     */
    public abstract boolean validate(final @NonNull ChunkSnapshot snapshot, final int x, final int y, final int z);

}
