package dev.kscott.quantum.rule.ruleset;

import dev.kscott.quantum.location.yvalidator.YValidator;
import dev.kscott.quantum.rule.QuantumRule;
import dev.kscott.quantum.rule.ruleset.search.SearchArea;
import org.checkerframework.checker.nullness.qual.NonNull;

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
    private final @NonNull YValidator yValidator;

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
            final @NonNull YValidator yValidator,
            final @NonNull SearchArea searchArea,
            final @NonNull List<QuantumRule> rules
    ) {
        this.worldUuid = worldUuid;
        this.yValidator = yValidator;
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
    public @NonNull YValidator getYValidator() {
        return yValidator;
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

    /**
     * Returns this ruleset's id
     *
     * @return id
     */
    public @NonNull String getId() {
        return id;
    }
}
