package sh.kaden.quantum.core.rule;

import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.option.Option;

import java.util.List;

/**
 * A rule used by Quantum to restrict location generation.
 */
public interface Rule {

    @NonNull String id();
    @NonNull List<Option<?>> options();
}
