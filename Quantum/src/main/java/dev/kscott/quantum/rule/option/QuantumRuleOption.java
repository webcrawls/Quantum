package dev.kscott.quantum.rule.option;

import dev.kscott.quantum.rule.QuantumRule;
import org.bukkit.persistence.PersistentDataContainer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;

public class QuantumRuleOption<T> {
    private final @NonNull String id;

    private T value;

    public QuantumRuleOption(final @NonNull String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

    public Type getType() {
        return getClass().getGenericSuperclass();
    }

    public void setValue(@Nullable Object value) {
        this.value = (T) value;
    }
}
