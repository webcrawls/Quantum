package dev.kscott.quantum.rule;

import dev.kscott.quantum.rule.rules.avoidBlock.AvoidBlockRule;
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

        this.registerRule("avoid-block", AvoidBlockRule.class);
    }

    /**
     * Registers a rule with a given id
     * @param id id of rule
     * @param ruleClass the class of the rule to regiser
     */
    public void registerRule(String id, Class<? extends QuantumRule> ruleClass) {
        this.ruleMap.put(id, ruleClass);
    }

    public @Nullable QuantumRule createFreshRule(String id) {
        if (!this.ruleMap.containsKey(id)) {
            return null;
        }

        Class<? extends QuantumRule> quantumRuleClass = ruleMap.get(id);
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