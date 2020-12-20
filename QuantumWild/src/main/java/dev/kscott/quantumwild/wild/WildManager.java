package dev.kscott.quantumwild.wild;

import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.location.QuantumLocation;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantumwild.IntegrationsManager;
import dev.kscott.quantumwild.config.Config;
import dev.kscott.quantumwild.config.Lang;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
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
     * A map where the key is a Player's UUID, and the value is a map where the key is a world's UUID, and the value is when the player can use /wild again
     */
    private final @NonNull Map<@NonNull UUID, @NonNull Map<@NonNull UUID, @NonNull Long>> perWorldCooldownMap;

    private final @NonNull Set<UUID> playersWarmingUp;

    /**
     * Config reference
     */
    private final @NonNull Config config;

    private final @NonNull IntegrationsManager integrationsManager;

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
     * @param config              {@link this#config}
     * @param integrationsManager {@link this#integrationsManager}
     * @param locationProvider    {@link this#locationProvider}
     * @param audiences           {@link this#audiences}
     * @param lang                {@link this#lang}
     * @param plugin              {@link this#plugin}
     */
    public WildManager(
            final @NonNull IntegrationsManager integrationsManager,
            final @NonNull Lang lang,
            final @NonNull BukkitAudiences audiences,
            final @NonNull Config config,
            final @NonNull LocationProvider locationProvider,
            final @NonNull JavaPlugin plugin
    ) {
        this.config = config;
        this.lang = lang;
        this.audiences = audiences;
        this.locationProvider = locationProvider;
        this.plugin = plugin;
        this.integrationsManager = integrationsManager;

        this.cooldownMap = new HashMap<>();
        this.perWorldCooldownMap = new HashMap<>();
        this.playersWarmingUp = new HashSet<>();
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
        if (!this.integrationsManager.isLuckPermsEnabled()) {
            return this.config.getFallbackCooldown();
        }

        final @NonNull CachedMetaData meta = this.integrationsManager.getLuckPerms().getUserManager().getUser(player.getUniqueId())
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
        if (this.config.isPerWorldCooldownEnabled()) {
            final @NonNull UUID playerUuid = player.getUniqueId();
            final @NonNull UUID worldUuid = player.getWorld().getUID();

            final @Nullable Map<UUID, Long> worldCooldownMap = this.perWorldCooldownMap.get(playerUuid);

            if (worldCooldownMap == null) {
                return 0L;
            }

            return worldCooldownMap.getOrDefault(worldUuid, 0L);
        } else {
            return this.cooldownMap.getOrDefault(player.getUniqueId(), 0L);
        }

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

        if (this.config.isPerWorldCooldownEnabled()) {
            if (!this.perWorldCooldownMap.containsKey(player.getUniqueId())) {
                this.perWorldCooldownMap.put(player.getUniqueId(), new HashMap<>());
            }

            final @NonNull Map<UUID, Long> map = this.perWorldCooldownMap.get(player.getUniqueId());

            map.put(player.getWorld().getUID(), timestamp);
        } else {
            this.cooldownMap.put(player.getUniqueId(), timestamp);
        }

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

    /**
     * Teleports a player, sends messages, errors, etc.
     *
     * @param player Player to teleport
     * @return A CompletableFuture<Boolean>, where the boolean is true if it was a successful teleport, or false if it failed (i.e. cooldown, invalid world, etc)
     */
    public @NonNull CompletableFuture<Boolean> wildTeleportPlayer(final @NonNull Player player) {
        @NonNull CompletableFuture<Boolean> cf = new CompletableFuture<>();

        final @NonNull World world = player.getWorld();

        final @Nullable QuantumRuleset ruleset = this.config.getRuleset(world);

        if (ruleset == null) {
            this.audiences.sender(player).sendMessage(lang.c("invalid-world"));
            cf.complete(false);
            return cf;
        }

        final @NonNull CompletableFuture<QuantumLocation> locationCf = this.locationProvider.getSpawnLocation(ruleset);

        if (canUseWild(player)) {
            if (this.config.isWarmupEnabled()) {
                this.audiences.sender(player).sendMessage(lang.c("warmup", Map.of("{time}", msToHms(this.config.getWarmupTime() * 1000L))));
                this.playersWarmingUp.add(player.getUniqueId());

                new BukkitRunnable() {
                    final int warmupTime = config.getWarmupTime();
                    int seconds = warmupTime;

                    @Override
                    public void run() {
                        if (!playersWarmingUp.contains(player.getUniqueId())) {
                            audiences.sender(player).sendMessage(lang.c("warmup-cancelled"));
                            cancel();
                            return;
                        }

                        if (seconds <= 0) {
                            teleportPlayer(locationCf, cf, player);
                            cancel();
                            playersWarmingUp.remove(player.getUniqueId());
                            return;
                        }

                        seconds--;
                    }
                }.runTaskTimer(plugin, 0, 20);
            } else {
                teleportPlayer(locationCf, cf, player);
            }
        } else {
            final long cooldown = getCurrentCooldown(player) - System.currentTimeMillis();

            this.audiences.sender(player).sendMessage(
                    lang.c("cooldown", Map.of(
                            "{time}", msToHms(cooldown)
                            )
                    )
            );

            cf.complete(false);
        }

        return cf;
    }

    /**
     * Converts milliseconds to "Xh Xm Xs" format
     * May also return "Xm Xs" or "Xs" if applicable
     *
     * @param ms milliseconds, as a long
     * @return formatted String
     */
    private @NonNull String msToHms(final long ms) {
        long hours = TimeUnit.MILLISECONDS.toHours(ms);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % TimeUnit.HOURS.toMinutes(1);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % TimeUnit.MINUTES.toSeconds(1);

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

        return timeBuilder.toString();
    }

    /**
     * Returns a Map, used for formatting placeholders
     *
     * @param location Location
     * @return A map
     */
    private @NonNull Map<String, String> locationToPlaceholderMap(final @NonNull Location location) {
        return Map.of(
                "{x}", Integer.toString(location.getBlockX()),
                "{y}", Integer.toString(location.getBlockY()),
                "{z}", Integer.toString(location.getBlockZ())
        );
    }

    private void teleportPlayer(
            final @NonNull CompletableFuture<QuantumLocation> cf,
            final @NonNull CompletableFuture<Boolean> successCf,
            final @NonNull Player player
    ) {
        cf.thenAccept(quantumLocation -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (quantumLocation.getLocation() == null) {
                        audiences.sender(player).sendMessage(lang.c("failed-spawn-location"));
                        return;
                    }

                    final @NonNull Location location = quantumLocation.getLocation();

                    if (config.isEssentialsIntegrationEnabled() && integrationsManager.isEssentialsEnabled()) {
                        final @NonNull CompletableFuture<Boolean> essCf = new CompletableFuture<>();

                        essCf.thenAccept(success -> {
                            successCf.complete(success);

                            if (success) {
                                applyWildCooldown(player);
                                audiences.sender(player).sendMessage(
                                        lang.c(
                                                "tp-success",
                                                locationToPlaceholderMap(location)
                                        )
                                );
                            }
                        });

                        integrationsManager.getEssentials().getUser(player)
                                .getAsyncTeleport()
                                .teleport(location.toCenterLocation(), null, PlayerTeleportEvent.TeleportCause.PLUGIN, essCf);

                    } else {
                        player.teleportAsync(location.toCenterLocation())
                                .thenAccept(success -> {
                                    if (success) {
                                        applyWildCooldown(player);
                                        audiences.sender(player).sendMessage(
                                                lang.c(
                                                        "tp-success",
                                                        locationToPlaceholderMap(location)
                                                )
                                        );
                                    } else {
                                        audiences.sender(player).sendMessage(
                                                lang.c(
                                                        "failed-spawn-location",
                                                        locationToPlaceholderMap(location)
                                                )
                                        );
                                    }
                                    successCf.complete(success);
                                });
                    }
                }
            }.runTask(plugin);
        });
    }

    /**
     * Invalidates a player's warmup, effectively cancelling it
     *
     * @param player Player
     */
    public void invalidateWarmup(final @NonNull Player player) {
        this.playersWarmingUp.remove(player.getUniqueId());
    }
}
