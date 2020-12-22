package dev.kscott.quantum.rule;

import dev.kscott.quantum.rule.rules.async.AvoidBlockRule;
import dev.kscott.quantum.rule.rules.async.OnlyBiomeRule;
import dev.kscott.quantum.rule.rules.async.OnlyBlockRule;
import dev.kscott.quantum.rule.rules.sync.AvoidEntityRule;
import dev.kscott.quantum.rule.rules.sync.NearbyEntityRule;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RuleRegistry {

    /**
     * The map where all rules are stored, by their id.
     */
    private final @NonNull Map<String, Class<? extends QuantumRule>> ruleMap;

    /**
     * Constructs the rule registry and it's internal map
     */
    public RuleRegistry() {
        this.ruleMap = new HashMap<>();

        // Async rules
        this.registerRule("avoid-block", AvoidBlockRule.class);
        this.registerRule("only-block", OnlyBlockRule.class);
        this.registerRule("only-biome", OnlyBiomeRule.class);

        // Sync rules
        this.registerRule("avoid-entity", AvoidEntityRule.class);
        this.registerRule("nearby-entity", NearbyEntityRule.class);
    }

    /**
     * Registers a rule with a given id
     *
     * @param id        id of rule
     * @param ruleClass the class of the rule to regiser
     */
    public void registerRule(String id, Class<? extends QuantumRule> ruleClass) {
        this.ruleMap.put(id, ruleClass);
    }

    /**
     * Constructs a fresh QuantumRule of the given id
     *
     * @param id id of QuantumRule
     * @return A new QuantumRule object
     */
    public @Nullable QuantumRule createFreshRule(String id) {
        if (!this.ruleMap.containsKey(id)) {
            return null;
        }

        return createFreshRule(ruleMap.get(id));
    }

    /**
     * Constructs a fresh QuantumRule
     *
     * @param quantumRuleClass class of QuantumRule
     * @return a new QuantumRule object
     */
    public @Nullable QuantumRule createFreshRule(final @NonNull Class<? extends QuantumRule> quantumRuleClass) {
        QuantumRule quantumRule;

        try {
            quantumRule = quantumRuleClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        quantumRule.postCreation();

        return quantumRule;
    }

    /**
     * Returns a Collection of all registered QuantumRules
     *
     * @return QuantumRule Collection
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
         * The id of the rule
         */
        private final @NonNull String id;

        /**
         * The class of the rule
         */
        private final @NonNull Class<? extends QuantumRule> ruleClass;

        /**
         * Constructs the EffectiveRule
         *
         * @param id        id of rule
         * @param ruleClass class of rule
         */
        private EffectiveRule(final @NonNull String id, final @NonNull Class<? extends QuantumRule> ruleClass) {
            this.id = id;
            this.ruleClass = ruleClass;
        }

        /**
         * Returns the rule's id
         *
         * @return String id
         */
        public @NonNull String getId() {
            return id;
        }

        /**
         * Returns the rule's class
         *
         * @return rule Class
         */
        public Class<? extends QuantumRule> getRuleClass() {
            return ruleClass;
        }

    }

}
