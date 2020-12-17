package dev.kscott.quantum.rule.ruleset;

import dev.kscott.quantum.location.locator.YLocator;
import dev.kscott.quantum.rule.AsyncQuantumRule;
import dev.kscott.quantum.rule.QuantumRule;
import dev.kscott.quantum.rule.SyncQuantumRule;
import dev.kscott.quantum.rule.ruleset.search.SearchArea;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuantumRuleset {

    /**
     * The id of this Ruleset
     */
    private final @NonNull String id;

    /**
     * The UUID of the world for this ruleset
     */
    private final @NonNull UUID worldUuid;

    /**
     * Where Quantum should prioritize spawning a player
     */
    private final @NonNull YLocator yLocator;

    /**
     * Where Quantum should search for spawns
     */
    private final @NonNull SearchArea searchArea;

    /**
     * The rules that apply to this ruleset
     */
    private final @NonNull List<QuantumRule> rules;

    public QuantumRuleset(
            final @NonNull String id,
            final @NonNull UUID worldUuid,
            final @NonNull YLocator yLocator,
            final @NonNull SearchArea searchArea,
            final @NonNull List<QuantumRule> rules
    ) {
        this.worldUuid = worldUuid;
        this.yLocator = yLocator;
        this.searchArea = searchArea;
        this.rules = rules;
        this.id = id;
    }

    /**
     * Returns the UUID of the world attached to this ruleset
     *
     * @return UUID
     */
    public @NonNull UUID getWorldUuid() {
        return worldUuid;
    }

    /**
     * Returns this ruleset's spawn target
     *
     * @return SpawnTarget
     */
    public @NonNull YLocator getYLocator() {
        return yLocator;
    }

    /**
     * Returns this ruleset's search area
     *
     * @return SearchArea
     */
    public @NonNull SearchArea getSearchArea() {
        return searchArea;
    }

    /**
     * Return the rules that apply to this ruleset
     *
     * @return List of QuantumRules (may be empty)
     */
    public @NonNull List<QuantumRule> getRules() {
        return rules;
    }

    public @NonNull List<AsyncQuantumRule> getAsyncRules() {
        final @NonNull List<AsyncQuantumRule> asyncRules = new ArrayList<>();

        for (final QuantumRule quantumRule : rules) {
            if (quantumRule instanceof AsyncQuantumRule) {
                asyncRules.add((AsyncQuantumRule) quantumRule);
            }
        }

        return asyncRules;
    }

    public @NonNull List<SyncQuantumRule> getSyncRules() {
        final @NonNull List<SyncQuantumRule> syncRules = new ArrayList<>();

        for (final QuantumRule quantumRule : rules) {
            if (quantumRule instanceof SyncQuantumRule) {
                syncRules.add((SyncQuantumRule) quantumRule);
            }
        }

        return syncRules;
    }

    /**
     * Returns this ruleset's id
     *
     * @return id
     */
    public @NonNull String getId() {
        return id;
    }
}
