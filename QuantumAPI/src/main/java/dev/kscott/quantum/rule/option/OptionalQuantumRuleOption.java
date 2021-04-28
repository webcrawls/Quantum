package dev.kscott.quantum.rule.option;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A QuantumRuleOption with an optional value.
 * If this value isn't defined, it will use {@link OptionalQuantumRuleOption#defaultValue}.
 *
 * @param <T>
 */
public class OptionalQuantumRuleOption<T> extends QuantumRuleOption<T> {

    /**
     * The default value to return if null.
     */
    private final T defaultValue;

    /**
     * Constructs OptionalQuantumRuleOption
     *
     * @param id           the id of this option
     * @param defaultValue
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

    public T getDefaultValue() {
        return defaultValue;
    }

}
