package dev.kscott.quantum.rule.ruleset;

import dev.kscott.quantum.rule.ruleset.search.SearchArea;
import dev.kscott.quantum.rule.ruleset.target.SpawnTarget;
import dev.kscott.quantum.rule.QuantumRule;
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
    private final @NonNull SpawnTarget spawnTarget;

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
            final @NonNull SpawnTarget spawnTarget,
            final @NonNull SearchArea searchArea,
            final @NonNull List<QuantumRule> rules
    ) {
        this.worldUuid = worldUuid;
        this.spawnTarget = spawnTarget;
        this.searchArea = searchArea;
        this.rules = rules;
        this.id = id;
    }

    public @NonNull UUID getWorldUuid() {
        return worldUuid;
    }

    public @NonNull SpawnTarget getSpawnTarget() {
        return spawnTarget;
    }

    public @NonNull SearchArea getSearchArea() {
        return searchArea;
    }

    public @NonNull List<QuantumRule> getRules() {
        return rules;
    }

    public @NonNull String getId() {
        return id;
    }
}
