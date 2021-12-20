package sh.kaden.quantum.core;

import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.location.LocationProvider;
import sh.kaden.quantum.core.rule.Rule;
import sh.kaden.quantum.core.world.World;

import java.util.List;

/**
 * Search settings used by the {@link LocationProvider}.
 */
public interface SearchSettings {

    /**
     * Returns the world to search in.
     *
     * @return the world
     */
    @NonNull World world();

    /**
     * Returns a list of rules to use for a search.
     *
     * @return the rules
     */
    @NonNull List<Rule> rules();

}
