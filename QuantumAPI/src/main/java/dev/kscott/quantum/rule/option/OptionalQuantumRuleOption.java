package dev.kscott.quantum.rule.option;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@link QuantumRuleOption} with an optional value.
 *
 * @param <T> type of this rule.
 */
public class OptionalQuantumRuleOption<T> extends QuantumRuleOption<T> {

    /**
     * The default value to return if null.
     */
    private final T defaultValue;

    /**
     * Constructs {@link OptionalQuantumRuleOption}
     *
     * @param id           the id of this option.
     * @param defaultValue default value to return if null.
     */
    public OptionalQuantumRuleOption(final @NonNull String id, final @NonNull T defaultValue) {
        super(id);
        this.defaultValue = defaultValue;
    }

    @Override
    public @NonNull T getValue() {
        if (this.value == null) {
            return getDefaultValue();
        }

        return this.value;
    }

    /**
     * Returns the default value.
     * @return the default value.
     */
    public T getDefaultValue() {
        return defaultValue;
    }

}
