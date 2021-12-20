package sh.kaden.quantum.core.location;

import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.SearchSettings;

import java.util.concurrent.CompletableFuture;

public interface LocationProvider {

    /**
     * Searches for a location using the provided rule information.
     *
     * @return the location in a {@link CompletableFuture} that completes
     * when the location is found
     */
    @NonNull CompletableFuture<BlockVector> findLocation(final @NonNull SearchSettings settings);

}
