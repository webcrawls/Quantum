package dev.kscott.quantum.rule.rules.sync;

import dev.kscott.quantum.rule.option.EntityListOption;
import dev.kscott.quantum.rule.option.RadiusOption;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashSet;

public class NearbyEntityRule extends SyncQuantumRule {

    public NearbyEntityRule() {
        super(new EntityListOption(), new RadiusOption());
    }

    @Override
    public boolean validate(@NonNull Chunk chunk, int x, int y, int z) {
        final @NonNull String[] entityTypes = this.getOption(EntityListOption.class).getValue();
        final int radius = this.getOption(RadiusOption.class).getValue();

        Collection<EntityType> types = new HashSet<>();

        for (String entityType : entityTypes) {
            types.add(EntityType.valueOf(entityType));
        }

        Collection<LivingEntity> entities = chunk.getBlock(x, y, z).getLocation().getNearbyLivingEntities(radius);

        for (final LivingEntity entity : entities) {
            if (types.contains(entity.getType())) {
                return true;
            }
        }

        return false;
    }

}
