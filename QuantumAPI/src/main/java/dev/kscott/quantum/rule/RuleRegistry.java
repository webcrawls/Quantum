package dev.kscott.quantum.rule;

import dev.kscott.quantum.rule.rules.async.AvoidAirRule;
import dev.kscott.quantum.rule.rules.async.AvoidBiomeRule;
import dev.kscott.quantum.rule.rules.async.AvoidBlockRule;
import dev.kscott.quantum.rule.rules.async.OnlyBiomeRule;
import dev.kscott.quantum.rule.rules.async.OnlyBlockRule;
import dev.kscott.quantum.rule.rules.sync.AvoidClaimsRule;
import dev.kscott.quantum.rule.rules.sync.AvoidEntityRule;
import dev.kscott.quantum.rule.rules.sync.AvoidRegionRule;
import dev.kscott.quantum.rule.rules.sync.NearbyEntityRule;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Contains a map of registered {@link QuantumRule}s
 */
public class RuleRegistry {

    /**
     * Stores all registered rules by their id.
     */
    private final @NonNull Map<String, Class<? extends QuantumRule>> ruleMap;

    /**
     * Constructs the rule registry and its internal map.
     *
     * @param plugin {@link JavaPlugin} instance.
     */
    public RuleRegistry(
            final @NonNull JavaPlugin plugin
    ) {
        this.ruleMap = new HashMap<>();

        // Async rules
        this.registerRule(AvoidBlockRule.class);
        this.registerRule(OnlyBlockRule.class);
        this.registerRule(AvoidBiomeRule.class);
        this.registerRule(OnlyBiomeRule.class);
        this.registerRule(AvoidAirRule.class);

        // Sync rules
        this.registerRule(AvoidEntityRule.class);
        this.registerRule(NearbyEntityRule.class);

        // WorldGuard rules
        if (plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            this.registerRule(AvoidRegionRule.class);
        }

        // Factions rules
        if (plugin.getServer().getPluginManager().isPluginEnabled("Factions")) {
            this.registerRule(AvoidClaimsRule.class);
        }
    }

    /**
     * Registers a {@link QuantumRule} class..
     *
     * @param ruleClass the {@link Class} of the {@link QuantumRule} to register.
     */
    public void registerRule(Class<? extends QuantumRule> ruleClass) {
        this.ruleMap.put(QuantumRule.getRuleId(ruleClass), ruleClass);
    }

    /**
     * Constructs a fresh {@link QuantumRule} of the given id.
     *
     * @param id id of {@link QuantumRule}.
     * @return A new {@link QuantumRule} object.
     */
    public @Nullable QuantumRule createFreshRule(String id) {
        if (!this.ruleMap.containsKey(id)) {
            return null;
        }

        return createFreshRule(ruleMap.get(id));
    }

    /**
     * Constructs a fresh {@link QuantumRule}.
     *
     * @param quantumRuleClass {@link Class} of {@link QuantumRule}.
     * @return a new {@link QuantumRule}.
     */
    public @Nullable QuantumRule createFreshRule(final @NonNull Class<? extends QuantumRule> quantumRuleClass) {
        QuantumRule quantumRule;

        try {
            quantumRule = quantumRuleClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        return quantumRule;
    }

    /**
     * Returns a {@link Collection} of all registered {@link QuantumRule}s.
     *
     * @return {@link Collection} of all registered {@link QuantumRule}s.
     */
    public @NonNull Collection<EffectiveRule> getRules() {
        final @NonNull Collection<EffectiveRule> effectiveRules = new HashSet<>();

        for (final Map.Entry<String, Class<? extends QuantumRule>> entry : this.ruleMap.entrySet()) {
            effectiveRules.add(new EffectiveRule(entry.getKey(), entry.getValue()));
        }

        return effectiveRules;
    }

    /**
     * An object which holds a rule's id and class.
     * Easier to work with.
     */
    public static class EffectiveRule {

        /**
         * The id of the rule.
         */
        private final @NonNull String id;

        /**
         * The class of the rule.
         */
        private final @NonNull Class<? extends QuantumRule> ruleClass;

        /**
         * Constructs {@link EffectiveRule}.
         *
         * @param id        id of rule.
         * @param ruleClass class of rule.
         */
        private EffectiveRule(final @NonNull String id, final @NonNull Class<? extends QuantumRule> ruleClass) {
            this.id = id;
            this.ruleClass = ruleClass;
        }

        /**
         * Returns the rule's id.
         *
         * @return id.
         */
        public @NonNull String getId() {
            return id;
        }

        /**
         * Returns the rule's class.
         *
         * @return {@link QuantumRule}'s {@link Class}.
         */
        public @NonNull Class<? extends QuantumRule> getRuleClass() {
            return ruleClass;
        }

    }

}
