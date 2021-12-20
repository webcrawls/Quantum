package sh.kaden.quantum.core.rule;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

/**
 * RuleRegistry stores a map of all rules that are usable by Quantum. If you intend on writing custom
 * rules, you must obtain an instance of this class and register the rule with {@link #register};
 */
public class RuleRegistry {

    private final @NonNull Map<String, Rule> rules;

    /**
     * Constructs {@code RuleRegistry}.
     */
    public RuleRegistry() {
        this.rules = new HashMap<>();
    }

    /**
     * Registers a rule.
     *
     * @param rule the rule to register
     * @throws IllegalArgumentException if there is a rule already registered under {@link Rule#id()}.
     */
    public void register(final @NonNull Rule rule) {
        final String id = rule.id();
        if (this.rules.containsKey(id)) {
            throw new IllegalArgumentException("Rule was already registered with this RuleRegistry. [id='" + id + "']");
        }

        this.rules.put(id, rule);
    }

    /**
     * Searches for a registered rule with the given id.
     *
     * @param id the id of the rule
     * @return the rule wrapped in an {@link Optional}
     */
    public @NonNull Optional<Rule> get(final @NonNull String id) {
        return Optional.ofNullable(this.rules.get(id));
    }

    /**
     * Searches for a registered rule with the given class.
     *
     * @param klazz the class of the rule
     * @return the rule wrapped in an {@link Optional}
     */
    public @NonNull <T extends Rule> Optional<Rule> get(final @NonNull Class<T> klazz) {
        for (final Rule rule : this.rules.values()) {
            if (rule.getClass().equals(klazz)) {
                return Optional.of(rule);
            }
        }

        return Optional.empty();
    }

    /**
     * Returns a list of all registered rules.
     *
     * @return the rules
     */
    public @NonNull List<Rule> rules() {
        return new ArrayList<>(this.rules.values());
    }


}
