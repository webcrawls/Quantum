package dev.kscott.quantum.rule.rules.sync;

import dev.kscott.quantum.rule.QuantumRule;
import dev.kscott.quantum.rule.option.QuantumRuleOption;
import org.bukkit.Chunk;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A QuantumRule that is ran on the main thread, thus has
 * access to the full Chunk.
 */
public abstract class SyncQuantumRule extends QuantumRule {

    /**
     * Constructs the SyncQuantumRule
     * @param options rule options list
     */
    public SyncQuantumRule(final @NonNull QuantumRuleOption<?>... options) {
        super(options);
    }

    /**
     * Checks the location against this rule and returns it's validity as a boolean.
     *
     * @param chunk the chunk
     * @param x     x coordinate, relative to chunk (0-15)
     * @param y     y coordinate (0-255)
     * @param z     z coordinate, relative to chunk (0-15)
     * @return true if valid, false if not
     */
    public abstract boolean validate(final @NonNull Chunk chunk, final int x, final int y, final int z);


}
