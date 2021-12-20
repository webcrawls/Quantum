package sh.kaden.quantum.core.option;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public interface Option<T> {

    static @NonNull <T> Option<T> of(final @NonNull String id,
                                     final @NonNull String description,
                                     final @NonNull Class<T> type) {
        return new BaseOption<T>(id, description, type);
    }

    static @NonNull <T> Option<T> of(final @NonNull String id,
                                     final @NonNull String description,
                                     final @NonNull Class<T> type,
                                     final @NonNull T defaultValue) {
        return new BaseOption<T>(id, description, type, defaultValue);
    }

    @NonNull String id();

    @NonNull String description();

    @NonNull Class<T> type();

    @NonNull Optional<T> defaultValue();

}
