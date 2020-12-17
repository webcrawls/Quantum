package dev.kscott.quantum.rule;

import dev.kscott.quantum.rule.option.QuantumRuleOption;
import org.bukkit.ChunkSnapshot;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class QuantumRule {

    private final @NonNull Map<String, QuantumRuleOption<?>> optionMap;

    public QuantumRule(final @NonNull QuantumRuleOption<?>... options) {

        optionMap = new HashMap<>();

        for (QuantumRuleOption<?> option : options) {
            optionMap.put(option.getId(), option);
        }
    }

    public QuantumRuleOption<?> getOption(String id) {
        return optionMap.get(id);
    }

    public <T> QuantumRuleOption<T> getOption(Class<? extends QuantumRuleOption<T>> quantumRuleOptionClass) {
        for (QuantumRuleOption<?> option : optionMap.values()) {
            if (option.getClass().isAssignableFrom(quantumRuleOptionClass)) {
                return (QuantumRuleOption<T>) option;
            }
        }

        return null;
    }

    public Collection<QuantumRuleOption<?>> getOptions() {
        return this.optionMap.values();
    }

    /**
     * Checks the location against this rule and returns it's validity as a boolean.
     * @param snapshot snapshot of the chunk
     * @param x x coordinate, relative to chunk (0-15)
     * @param y y coordinate (0-255)
     * @param z z coordinate, relative to chunk (0-15)
     * @return true if valid, false if not
     */
    public abstract boolean validate(final @NonNull ChunkSnapshot snapshot, final int x, final int y, final int z);

}
