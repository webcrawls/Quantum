package dev.kscott.quantum.rule;

import com.google.common.base.CaseFormat;
import dev.kscott.quantum.rule.option.QuantumRuleOption;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The base QuantumRule class.
 */
public abstract class QuantumRule {

    /**
     * The options map
     */
    private final @NonNull Map<String, QuantumRuleOption<?>> optionMap;

    /**
     * Constructs QuantumRule
     *
     * @param options the rules options
     */
    public QuantumRule(final @NonNull QuantumRuleOption<?>... options) {
        optionMap = new HashMap<>();

        for (QuantumRuleOption<?> option : options) {
            optionMap.put(option.getId(), option);
        }
    }

    /**
     * Called after the rule is created ({@link RuleRegistry#createFreshRule(Class)}.
     * This method should be used to parse options.
     */
    public void postCreation() {
    }

    /**
     * Returns an option with a specified id
     *
     * @param id the id of the option
     * @return QuantumRuleOption
     */
    public @Nullable QuantumRuleOption<?> getOption(String id) {
        return optionMap.get(id);
    }

    /**
     * Returns an option with the class
     *
     * @param quantumRuleOptionClass class to get option for
     * @param <T>                    the option's type
     * @return QuantumRuleOption
     */
    public <T> QuantumRuleOption<T> getOption(Class<? extends QuantumRuleOption<T>> quantumRuleOptionClass) {
        for (QuantumRuleOption<?> option : optionMap.values()) {
            if (option.getClass().isAssignableFrom(quantumRuleOptionClass)) {
                return (QuantumRuleOption<T>) option;
            }
        }

        return null;
    }

    /**
     * @return A Collection of QuantumRuleOption
     */
    public Collection<QuantumRuleOption<?>> getOptions() {
        return this.optionMap.values();
    }

    /**
     * Returns this rule's id.
     *
     * @return Rule id string (formatted "like-this")
     */
    public static String getRuleId(final @NonNull Class<? extends QuantumRule> clazz) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, clazz.getSimpleName().replace("Rule", ""));
    }
}
