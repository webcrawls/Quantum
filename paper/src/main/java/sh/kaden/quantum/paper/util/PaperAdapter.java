package sh.kaden.quantum.paper.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.world.block.BlockType;
import sh.kaden.quantum.core.world.chunk.Chunk;
import sh.kaden.quantum.core.location.BlockVector;
import sh.kaden.quantum.paper.world.PaperWorld;
import sh.kaden.quantum.core.world.World;
import sh.kaden.quantum.paper.world.chunk.PaperChunk;

import java.util.Optional;

/**
 * A set of utility methods for converting between equivalent Paper and Quantum classes.
 */
public final class PaperAdapter {

    /**
     * Returns a new {@link BlockVector} using values from {@link Location}.
     *
     * @param location the location
     * @return the block vector
     */
    public static @NonNull BlockVector ofBukkitLocation(final @NonNull Location location) {
        return BlockVector.of(location.getX(), location.getY(), location.getZ());
    }

    /**
     * Searches for a {@link org.bukkit.World} equivalent to the provided Quantum {@link World}.
     *
     * @param world the world
     * @return the world wrapped in an {@link Optional}.
     */
    public static @NonNull Optional<org.bukkit.@NonNull World> toBukkitWorld(final @NonNull World world) {
        return Optional.ofNullable(Bukkit.getServer().getWorld(world.name()));
    }

    /**
     * Creates a Quantum {@link World} using the Bukkit {@link org.bukkit.World}.
     *
     * @param world the world
     * @return the world wrapped in an {@link Optional}.
     */
    public static @NonNull World ofBukkitWorld(final org.bukkit.@NonNull World world) {
        return new PaperWorld(world);
    }

    /**
     * Creates a Quantum {@link Chunk} using the Bukkit {@link org.bukkit.Chunk}.
     *
     * @param chunk the chunk
     * @return the chunk wrapped in an {@link Optional}.
     */
    public static @NonNull Chunk fromBukkitChunk(final org.bukkit.@NonNull Chunk chunk) {
        return new PaperChunk(chunk);
    }

    /**
     * Creates a {@link BlockType} from a {@link Material}.
     *
     * @param material the material
     * @return the block type
     */
    public static @NonNull BlockType fromBukkitMaterial(final @NonNull Material material) {
        return new BlockType(material.name());
    }

    /**
     * Returns the value of the {@link BlockType}'s material.
     *
     * @param blockType the block type
     * @return the material
     */
    public static @NonNull Material toBukkitMaterial(final @NonNull BlockType blockType) {
        return Material.valueOf(blockType.id());
    }


}
