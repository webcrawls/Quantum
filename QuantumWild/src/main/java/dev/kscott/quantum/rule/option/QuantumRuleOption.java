package dev.kscott.quantum.rule.option;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 *
 * @param <T> The type of the value
 */
public class QuantumRuleOption<T> {

    private final @NonNull String id;

    Class<T> type;

    Class<T> value;

}
