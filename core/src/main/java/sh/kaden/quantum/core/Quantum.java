package sh.kaden.quantum.core;

import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.location.LocationProvider;
import sh.kaden.quantum.core.rule.RuleRegistry;

/**
 * The main interface used for accessing Quantum functionality.
 */
public interface Quantum {

    /**
     * Returns the rule registry.
     *
     * @return the rule registry
     */
    @NonNull RuleRegistry ruleRegistry();

    /**
     * Returns the location provider.
     *
     * @return the location provider
     */
    @NonNull LocationProvider locationProvider();

}
