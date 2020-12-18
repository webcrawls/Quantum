package dev.kscott.quantum.api;

import com.google.inject.Inject;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.rule.RuleRegistry;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The API class. This should only be interacted with by other plugins/dependants.
 */
public class QuantumAPI {

    private final @NonNull LocationProvider locationProvider;

    private final @NonNull RulesetRegistry rulesetRegistry;

    private final @NonNull RuleRegistry ruleRegistry;

    @Inject
    public QuantumAPI(final @NonNull LocationProvider locationProvider, final @NonNull RulesetRegistry rulesetRegistry, final @NonNull RuleRegistry ruleRegistry) {
        this.locationProvider = locationProvider;
        this.rulesetRegistry = rulesetRegistry;
        this.ruleRegistry = ruleRegistry;
    }

    public LocationProvider getLocationProvider() {
        return locationProvider;
    }

    public RulesetRegistry getRulesetRegistry() {
        return rulesetRegistry;
    }

    public RuleRegistry getRuleRegistry() {
        return ruleRegistry;
    }
}
