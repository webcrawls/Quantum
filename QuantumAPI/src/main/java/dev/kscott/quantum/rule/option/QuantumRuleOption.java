package dev.kscott.quantum.rule.option;

import com.google.common.reflect.TypeToken;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * The base QuantumRuleOption
 *
 * @param <T> The value's type
 */
public class QuantumRuleOption<T> {

    /**
     * Id of this option
     */
    private final @NonNull String id;

    /**
     * The value of this option
     */
    protected @MonotonicNonNull T value;

    /**
     * Constructs the QuantumRuleOption
     *
     * @param id id of this option
     */
    public QuantumRuleOption(final @NonNull String id) {
        this.id = id;
    }

    /**
     * Returns this option's id
     *
     * @return id
     */
    public @NonNull String getId() {
        return id;
    }

    /**
     * Returns this value
     *
     * @return value (may be null if it hasn't been loaded by the ruleset loader yet)
     */
    public @NonNull T getValue() {
        return value;
    }

    /**
     * Returns the TypeToken of this class
     *
     * @return TypeToken
     */
    public @NonNull TypeToken<T> getTypeToken() {
        return new TypeToken<T>(getClass()) {
        };
    }

    /**
     * Sets the value of this class
     *
     * @param value value
     */
    public void setValue(@Nullable Object value) {
        this.value = (T) value;
    }
}
