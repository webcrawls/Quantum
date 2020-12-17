package dev.kscott.quantum.rule.ruleset;

import dev.kscott.quantum.rule.QuantumRule;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

/**
 * The main class that controls access and stores rulesets.
 * If a ruleset isn't registered here, it doesn't to Quantum.
 */
public class RulesetRegistry {

    /**
     * The map where all rulesets are stored
     */
    private final @NonNull Map<String, QuantumRuleset> rulesetMap;

    /**
     * Constructs the RulesetRegistry and it's internal map
     */
    public RulesetRegistry() {
        this.rulesetMap = new HashMap<>();
    }

    /**
     * Registers a Ruleset under it's id
     * @param ruleset Ruleset to register
     * @throws IllegalStateException if a ruleset with the same id is already registered
     */
    public void register(final @NonNull QuantumRuleset ruleset) throws IllegalStateException {
        final @NonNull String id = ruleset.getId();

        if (rulesetMap.containsKey(id)) {
            throw new IllegalStateException("Ruleset with the id '"+id+"' is already registered!");
        }

        this.rulesetMap.put(id, ruleset);
    }

    /**
     * Returns a ruleset
     * @param id id of the ruleset
     * @return ruleset, may be null if none was registered
     */
    public QuantumRuleset getRuleset(final @NonNull String id) {
        return this.rulesetMap.get(id);
    }

    /**
     * Returns all registered rulesets
     * @return a list of QuantumRuleset
     */
    public Collection<QuantumRuleset> getRulesets() {
        return this.rulesetMap.values();
    }

}
