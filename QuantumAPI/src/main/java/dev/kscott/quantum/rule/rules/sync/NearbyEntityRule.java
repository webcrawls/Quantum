package dev.kscott.quantum.rule.rules.sync;

import dev.kscott.quantum.rule.option.EntityListOption;
import dev.kscott.quantum.rule.option.RadiusOption;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A rule which requires a specific entity to be within
 * radius to consider a location valid.
 */
public class NearbyEntityRule extends SyncQuantumRule {

    /**
     * A Set of EntityTypes to require nearby
     */
    private @MonotonicNonNull Set<EntityType> entityTypes;

    /**
     * The radius of how far away QuantumAPI should search for entities
     */
    private int radius;

    /**
     * Constructs the NearbyEntityRule
     */
    public NearbyEntityRule() {
        super(new EntityListOption(), new RadiusOption());
    }

    /**
     * Loads {@link this#entityTypes}, {@link this#radius}
     */
    @Override
    public void postCreation() {
        final @NonNull String[] entityTypeIds = this.getOption(EntityListOption.class).getValue();

        entityTypes = new HashSet<>();

        for (final @NonNull String entityType : entityTypeIds) {
            entityTypes.add(EntityType.valueOf(entityType));
        }

        radius = this.getOption(RadiusOption.class).getValue();
    }

    /**
     * Checks if a given EntityType is nearby.
     *
     * @param chunk the chunk
     * @param x     x coordinate, relative to chunk (0-15)
     * @param y     y coordinate (0-255)
     * @param z     z coordinate, relative to chunk (0-15)
     * @return true if the entity is nearby, false if isn't
     */
    @Override
    public boolean validate(@NonNull Chunk chunk, int x, int y, int z) {
        final @NonNull Collection<LivingEntity> entities = chunk.getBlock(x, y, z).getLocation().getNearbyLivingEntities(radius);

        for (final LivingEntity entity : entities) {
            if (entityTypes.contains(entity.getType())) {
                return true;
            }
        }

        return false;
    }

}
