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

public class NearbyEntityRule extends SyncQuantumRule {

    private @MonotonicNonNull Set<EntityType> entityTypes;

    private int radius;

    public NearbyEntityRule() {
        super(new EntityListOption(), new RadiusOption());
    }

    @Override
    public void postCreation() {
        final @NonNull String[] entityTypeIds = this.getOption(EntityListOption.class).getValue();

        entityTypes = new HashSet<>();

        for (final @NonNull String entityType : entityTypeIds) {
            entityTypes.add(EntityType.valueOf(entityType));
        }

        radius = this.getOption(RadiusOption.class).getValue();
    }

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
