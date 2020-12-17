package dev.kscott.quantum.rule.rules.avoidBlock;

import dev.kscott.quantum.rule.QuantumRule;
import dev.kscott.quantum.rule.option.QuantumRuleOption;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AvoidBlockRule extends QuantumRule {

    public AvoidBlockRule() {
        super(new BlockListOption());
    }

    @Override
    public boolean validate(@NonNull ChunkSnapshot snapshot, int x, int y, int z) {
        System.out.println("test");
        final QuantumRuleOption<String[]> blockListOption = this.getOption(BlockListOption.class);

        System.out.println(Arrays.toString(blockListOption.getValue()));

        final Set<Material> materials = new HashSet<>();

        for (String materialId : blockListOption.getValue()) {
            materials.add(Material.getMaterial(materialId));
            System.out.println(Material.getMaterial(materialId));
        }

        final @NonNull BlockData blockData = snapshot.getBlockData(x, y-1, z);

        return !materials.contains(blockData.getMaterial());
    }
}
