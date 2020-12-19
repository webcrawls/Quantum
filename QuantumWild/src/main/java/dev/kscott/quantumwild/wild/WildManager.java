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

    /**
     * A map where the key is a Player's UUID, and the value is when the player can use /wild again (in ms)
     */
    private final @NonNull Map<@NonNull UUID, @NonNull Long> cooldownMap;

    /**
     * LuckPerms reference
     */
    private final @Nullable LuckPerms luckPerms;

    /**
     * Config reference
     */
    private final @NonNull Config config;

    /**
     * Constucts WildManager
     *
     * @param config    {@link this#config}
     * @param luckPerms {@link this#luckPerms}
     */
    public WildManager(final @NonNull Config config, final @Nullable LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
        this.config = config;

        this.cooldownMap = new HashMap<>();
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

        final @Nullable String cooldownValue = meta.getMetaValue("quantum.wild.cooldown");

        if (cooldownValue == null) {
            return 0;
        }

        return Integer.parseInt(cooldownValue);
    }

    /**
     * Returns when a player's cooldown will be up (in ms)
     *
     * @param player Player to check
     * @return the timestamp of when their cooldown will be up, in ms will return 0 if there is no cooldown applied
     */
    public long getCurrentCooldown(final @NonNull Player player) {
        return this.cooldownMap.getOrDefault(player.getUniqueId(), 0L);
    }

    /**
     * Applies /wild cooldown to a Player, using the value from
     * {@link this#getCooldownToApply(Player)}
     * @param player Player to apply cooldown for
     */
    public void applyWildCooldown(final @NonNull Player player) {
        final long ms = getCooldownToApply(player) * 1000L;

        final long timestamp = System.currentTimeMillis() + ms;

        this.cooldownMap.put(player.getUniqueId(), timestamp);
    }

    /**
     * Checks if the Player's cooldown has expired
     * @param player Player
     * @return true they can
     */
    public boolean canUseWild(final @NonNull Player player) {
        final long now = System.currentTimeMillis();

        return now >= getCurrentCooldown(player);
    }
}
