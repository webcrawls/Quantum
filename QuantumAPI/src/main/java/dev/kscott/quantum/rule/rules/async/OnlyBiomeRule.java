package dev.kscott.quantum.rule.rules.async;

import dev.kscott.quantum.rule.option.BiomeListOption;
import org.bukkit.ChunkSnapshot;
import org.bukkit.block.Biome;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashSet;
import java.util.Set;

public class OnlyBiomeRule extends AsyncQuantumRule {

    /**
     * Constructs OnlyBiomeRule
     */
    public OnlyBiomeRule() {
        super(new BiomeListOption());
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
        final @NonNull String[] biomeIds = this.getOption(BiomeListOption.class).getValue();

        final Set<Biome> biomes = new HashSet<>();

        for (String biomeId : biomeIds) {
            biomes.add(Biome.valueOf(biomeId));
        }

        final @NonNull Biome biome = snapshot.getBiome(x, y, z);

        return biomes.contains(biome);
    }

}
