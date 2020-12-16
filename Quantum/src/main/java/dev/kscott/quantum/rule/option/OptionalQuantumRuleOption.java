package dev.kscott.quantum.rule.option;

import org.checkerframework.checker.nullness.qual.NonNull;

public class OptionalQuantumRuleOption<T> extends QuantumRuleOption<T> {

    private T value;

    private final T defaultValue;

    public OptionalQuantumRuleOption(final @NonNull String id, final @NonNull T defaultValue) {
        super(id);
        this.defaultValue = defaultValue;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

}
