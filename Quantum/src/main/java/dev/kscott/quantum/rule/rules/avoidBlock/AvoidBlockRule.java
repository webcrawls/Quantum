package dev.kscott.quantum.rule.rules.avoidBlock;

import dev.kscott.quantum.rule.QuantumRule;
import dev.kscott.quantum.rule.option.QuantumRuleOption;
import org.bukkit.ChunkSnapshot;
import org.bukkit.block.data.BlockData;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Map;

public class AvoidBlockRule extends QuantumRule {

    public AvoidBlockRule() {
        super(new BlockListOption());
    }

    @Override
    public boolean validate(@NonNull ChunkSnapshot snapshot, int x, int y, int z) {
        final QuantumRuleOption<String[]> blockListOption = this.getOption(BlockListOption.class);

        System.out.println(Arrays.toString(blockListOption.getValue()));

        final @NonNull BlockData blockData = snapshot.getBlockData(x, y, z);

        return true;
    }
}
