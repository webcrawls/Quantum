package dev.kscott.quantumspawn.listeners;

import dev.kscott.quantumspawn.config.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The PlayerJoinListener.
 */
public class PlayerJoinListener implements Listener {

    /**
     * Config reference.
     */
    private final @NonNull Config config;

    /**
     * Constructs PlayerJoinListener.
     *
     * @param config Config reference.
     */
    public PlayerJoinListener(final @NonNull Config config) {
        this.config = config;
    }

    /**
     * Gives players a random spawn on join.
     * @param event PlayerJoinEvent.
     */
    @EventHandler
    public void onPlayerJoin(final @NonNull PlayerJoinEvent event) {

    }

}
