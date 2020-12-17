package dev.kscott.quantum.rule;

import dev.kscott.quantum.rule.rules.async.AvoidBlockRule;
import dev.kscott.quantum.rule.rules.AvoidEntityRule;
import dev.kscott.quantum.rule.rules.async.OnlyBlockRule;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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

        // Sync rules
        this.registerRule("avoid-entity", AvoidEntityRule.class);
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

        return quantumRule;
    }

}
