package dev.kscott.quantumspawn.listeners;

import com.google.inject.Inject;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.location.QuantumLocation;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantumspawn.config.Config;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Listens on player death-related events.
 */
public class PlayerDeathListener implements Listener {

    /**
     * Config reference.
     */
    private final @NonNull Config config;

    /**
     * JavaPlugin reference.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * LocationProvider reference.
     */
    private final @NonNull LocationProvider locationProvider;

    /**
     * Constructs PlayerJoinListener.
     *
     * @param locationProvider LocationProvider reference.
     */
    @Inject
    public PlayerDeathListener(
            final @NonNull JavaPlugin plugin,
            final @NonNull Config config,
            final @NonNull LocationProvider locationProvider
    ) {
        this.config = config;
        this.plugin = plugin;
        this.locationProvider = locationProvider;
    }

    /**
     * Handles the {@link PlayerRespawnEvent} and teleports them if enabled.
     *
     * @param event {@link PlayerRespawnEvent}.
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        final @NonNull Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            return;
        }

        if (config.isGoToBedEnabled() && player.getBedSpawnLocation() != null) {
            return;
        }

        final @NonNull World world = this.config.isDefaultWorldEnabled() ? this.config.getDefaultWorld() : player.getWorld();

        final @Nullable QuantumRuleset ruleset = config.getRuleset(world);

        if (ruleset == null) {
            return;
        }

        final @NonNull CompletableFuture<QuantumLocation> locationCf = this.locationProvider.getLocation(ruleset);

        locationCf.thenAccept(quantumLocation -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleportAsync(QuantumLocation.toCenterHorizontalLocation(quantumLocation.getLocation()));
                }
            }.runTask(plugin);
        });
    }

}
