package dev.kscott.quantumwild.wild;

import com.earth2me.essentials.Essentials;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantumwild.config.Config;
import dev.kscott.quantumwild.config.Lang;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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

    private final @Nullable Essentials essentials;

    /**
     * JavaPlugin reference
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * BukkitAudiences reference
     */
    private final @NonNull BukkitAudiences audiences;

    /**
     * Lang reference
     */
    private final @NonNull Lang lang;


    private final @NonNull LocationProvider locationProvider;

    /**
     * Constructs WildManager
     *
     * @param config    {@link this#config}
     * @param luckPerms {@link this#luckPerms}
     */
    public WildManager(
            final @NonNull LuckPerms luckPerms,
            final @NonNull Essentials essentials,
            final @NonNull Lang lang,
            final @NonNull BukkitAudiences audiences,
            final @NonNull Config config,
            final @NonNull LocationProvider locationProvider,
            final @NonNull JavaPlugin plugin
    ) {
        this.luckPerms = luckPerms;
        this.config = config;
        this.lang = lang;
        this.audiences = audiences;
        this.locationProvider = locationProvider;
        this.plugin = plugin;
        this.essentials = essentials;

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
     *
     * @param player Player to apply cooldown for
     */
    public void applyWildCooldown(final @NonNull Player player) {
        final long ms = getCooldownToApply(player) * 1000L;

        final long timestamp = System.currentTimeMillis() + ms;

        this.cooldownMap.put(player.getUniqueId(), timestamp);
    }

    /**
     * Checks if the Player's cooldown has expired
     *
     * @param player Player
     * @return true they can
     */
    public boolean canUseWild(final @NonNull Player player) {
        final long now = System.currentTimeMillis();

        return now >= getCurrentCooldown(player);
    }

    public @NonNull CompletableFuture<Boolean> wildTeleportPlayer(final @NonNull Player player) {
        final @NonNull CompletableFuture<Boolean> cf = new CompletableFuture<>();

        final @NonNull World world = player.getWorld();

        final @Nullable QuantumRuleset ruleset = this.config.getRuleset(world);

        if (ruleset == null) {
            this.audiences.sender(player).sendMessage(lang.c("invalid_world"));
            cf.complete(false);
            return cf;
        }

        if (canUseWild(player)) {
            this.locationProvider.getSpawnLocation(ruleset)
                    .thenAccept(quantumLocation -> new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (quantumLocation.getLocation() == null) {
                                audiences.sender(player).sendMessage(lang.c("failed_spawn_location"));
                                return;
                            }

                            final @NonNull Location location = quantumLocation.getLocation();

                            player.teleportAsync(location.toCenterLocation())
                                    .thenAccept(success -> {
                                        cf.complete(success);
                                        if (success) {
                                            applyWildCooldown(player);
                                            audiences.sender(player).sendMessage(
                                                    lang.c(
                                                            "tp_success",
                                                            Map.of(
                                                                    "{x}", Integer.toString(location.getBlockX()),
                                                                    "{y}", Integer.toString(location.getBlockY()),
                                                                    "{z}", Integer.toString(location.getBlockZ())
                                                            )
                                                    )
                                            );
                                        }
                                    });

                        }
                    }.runTask(plugin));
        } else {
            final long cooldown = getCurrentCooldown(player) - System.currentTimeMillis();

            long hours = TimeUnit.MILLISECONDS.toHours(cooldown);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldown) % TimeUnit.HOURS.toMinutes(1);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldown) % TimeUnit.MINUTES.toSeconds(1);

            final @NonNull StringBuilder timeBuilder = new StringBuilder();

            if (hours != 0) {
                timeBuilder.append(hours).append("h");
            }

            if (minutes != 0) {
                timeBuilder.append(timeBuilder.length() == 0 ? "" : " ").append(minutes).append("m");
            }

            if (seconds != 0) {
                timeBuilder.append(timeBuilder.length() == 0 ? "" : " ").append(seconds).append("s");
            }

            this.audiences.sender(player).sendMessage(
                    lang.c("cooldown", Map.of(
                            "{time}", timeBuilder.toString()
                    ))
            );
            cf.complete(false);
        }

        return cf;
    }
}
