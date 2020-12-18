package dev.kscott.quantum.rule.option;

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
