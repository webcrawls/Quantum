package dev.kscott.quantum.rule.option;

import com.google.common.reflect.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;

public class QuantumRuleOption<T> {
    private final @NonNull String id;

    private T value;

    private final boolean isList;

    public QuantumRuleOption(final @NonNull String id, final boolean isList) {
        this.id = id; this.isList = isList;
    }

    public @NonNull String getId() {
        return id;
    }

    public @NonNull T getValue() {
        return value;
    }

    public @NonNull TypeToken<T> getTypeToken() {
        return new TypeToken<T>(getClass()){};
    }

    public void setValue(@Nullable Object value) {
        System.out.println("setValue: "+value);
        this.value = (T) value;
    }

    public boolean isList() {
        return isList;
    }
}
