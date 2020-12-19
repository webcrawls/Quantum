package dev.kscott.quantumwild.wild;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Provides methods used by player-led interactions with the
 * /wild command.
 */
public class WildManager {

    private final @NonNull Map<@NonNull UUID, @NonNull Long> cooldownMap;

    public WildManager() {
        this.cooldownMap = new HashMap<>();
    }

    public long getCooldownToApply(final @NonNull Player player) {
        System.out.println(player.getEffectivePermissions());
        return 0;
    }

    public long getCurrentCooldown(final @NonNull Player player) {
        return 0;
    }

    public void setCooldown(final @NonNull Player player) {
    }
}
