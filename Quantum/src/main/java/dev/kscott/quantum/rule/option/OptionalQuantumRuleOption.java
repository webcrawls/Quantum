package dev.kscott.quantum.rule.option;

import org.checkerframework.checker.nullness.qual.NonNull;

public class OptionalQuantumRuleOption<T> extends QuantumRuleOption<T> {

    private final T defaultValue;

    public OptionalQuantumRuleOption(final @NonNull String id, final boolean isList, final @NonNull T defaultValue) {
        super(id, isList);
        this.defaultValue = defaultValue;
    }

    // TODO override getValue and return default value if value is null

    public T getDefaultValue() {
        return defaultValue;
    }

}
