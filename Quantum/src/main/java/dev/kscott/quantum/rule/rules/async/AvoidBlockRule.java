package dev.kscott.quantum.rule.rules.async;

import dev.kscott.quantum.rule.AsyncQuantumRule;
import dev.kscott.quantum.rule.QuantumRule;
import dev.kscott.quantum.rule.option.BlockListOption;
import dev.kscott.quantum.rule.option.QuantumRuleOption;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * A rule that invalidates a location if y-1 is a specific material
 */
public class AvoidBlockRule extends AsyncQuantumRule {

    /**
     * Constructs AvoidBlockRule
     */
    public AvoidBlockRule() {
        super(new BlockListOption());
    }

    /**
     * Checks if the material of y-1 is contained within the BlockListOption.
     *
     * @param snapshot snapshot of the chunk
     * @param x        x coordinate, relative to chunk (0-15)
     * @param y        y coordinate (0-255)
     * @param z        z coordinate, relative to chunk (0-15)
     * @return true if valid, false if not
     */
    public boolean validate(@NonNull ChunkSnapshot snapshot, int x, int y, int z) {

        final QuantumRuleOption<String[]> blockListOption = this.getOption(BlockListOption.class);

        final Set<Material> materials = new HashSet<>();

        for (String materialId : blockListOption.getValue()) {
            materials.add(Material.getMaterial(materialId));
        }

        final @NonNull BlockData blockData = snapshot.getBlockData(x, y - 1, z);

        return !materials.contains(blockData.getMaterial());
    }
}
