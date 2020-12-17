package dev.kscott.quantum.rule.rules.avoidBlock;

import dev.kscott.quantum.rule.option.QuantumRuleOption;

/**
 * An option that holds a list of block material names
 */
public class BlockListOption extends QuantumRuleOption<String[]> {

    /**
     * Constructs BlockListOption
     */
    public BlockListOption() {
        super("block-types");
    }

}
