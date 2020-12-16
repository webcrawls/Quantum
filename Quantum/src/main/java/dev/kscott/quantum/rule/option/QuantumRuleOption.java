package dev.kscott.quantum.rule.option;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 *
 * @param <T> The type of the value
 */
public class QuantumRuleOption<T> {

    private final @NonNull String id;

    T value;

    public QuantumRuleOption(final @NonNull String id, final @NonNull T value) {
        this.id = id;
        this.value = value;
    }
}
