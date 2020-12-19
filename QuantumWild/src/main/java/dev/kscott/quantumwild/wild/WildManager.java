package dev.kscott.quantumwild.wild;

import dev.kscott.quantumwild.config.Config;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Provides methods used by player-led interactions with the
 * /wild command.
 */
public class WildManager {

    private final @NonNull Map<@NonNull UUID, @NonNull Long> cooldownMap;

    private final @Nullable LuckPerms luckPerms;

    private final @NonNull Config config;

    public WildManager(final @NonNull Config config, final @Nullable LuckPerms luckPerms) {
        this.cooldownMap = new HashMap<>();
        this.luckPerms = luckPerms;
        this.config = config;
    }

    /**
     * Returns the amount of cooldown that this player will receive.
     * This takes the player's world into consideration, and the value
     * of {@link Config#isPerWorldCooldownEnabled()}
     *
     * @param player Player to get cooldown for
     * @return amount of cooldown, in seconds
     */
    public int getCooldownToApply(final @NonNull Player player) {
        if (this.luckPerms == null) {
            return this.config.getFallbackCooldown();
        }

        final @NonNull CachedMetaData meta = this.luckPerms.getUserManager().getUser(player.getUniqueId())
                .getCachedData()
                .getMetaData();

        final @NonNull World world = player.getWorld();

        if (this.config.isPerWorldCooldownEnabled()) {
            final @Nullable String cooldownValue = meta.getMetaValue("quantum.wild.cooldown." + world.getName());

            if (cooldownValue == null) {
                return 0;
            }

            return Integer.parseInt(cooldownValue);
        }

        final @Nullable String cooldownValue = meta.getMetaValue(("quantum.wild.cooldown"));

        if (cooldownValue == null) {
            return 0;
        }

        return Integer.parseInt(cooldownValue);
    }

    public long getCurrentCooldown(final @NonNull Player player) {
        return 0;
    }

    public void setCooldown(final @NonNull Player player) {
    }
}
