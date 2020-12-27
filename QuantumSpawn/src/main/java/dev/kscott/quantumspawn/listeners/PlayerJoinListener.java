package dev.kscott.quantumspawn.listeners;

import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.location.QuantumLocation;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantumspawn.config.Config;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * The PlayerJoinListener.
 */
public class PlayerJoinListener implements Listener {

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
    public PlayerJoinListener(
            final @NonNull JavaPlugin plugin,
            final @NonNull Config config,
            final @NonNull LocationProvider locationProvider
    ) {
        this.plugin = plugin;
        this.config = config;
        this.locationProvider = locationProvider;
    }

    /**
     * Gives players a random spawn on join.
     *
     * @param event PlayerJoinEvent.
     */
    @EventHandler
    public void onPlayerJoin(final @NonNull PlayerJoinEvent event) {
        final @NonNull Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            return;
        }

        final @NonNull World world = player.getWorld();

        final @Nullable QuantumRuleset ruleset = config.getRuleset(world);

        if (ruleset == null) {
            return;
        }

        final @Nullable QuantumLocation quantumLocation = this.locationProvider.getQueueLocation(ruleset);

        if (quantumLocation == null) {
            this.plugin.getLogger().warning("The location queue was empty for " + ruleset.getId() + ", so " + player.getName() + " was not teleported. Please set " + ruleset.getId() + "'s queue target to a non-zero number to fix this.");
            return;
        }

        player.teleportAsync(quantumLocation.getLocation());
    }

}
