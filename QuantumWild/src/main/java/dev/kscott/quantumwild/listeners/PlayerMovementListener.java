package dev.kscott.quantumwild.listeners;

import com.google.inject.Inject;
import dev.kscott.quantumwild.wild.WildManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerMovementListener implements Listener {

    private final @NonNull WildManager wildManager;

    @Inject
    public PlayerMovementListener(final @NonNull WildManager wildManager) {
        this.wildManager = wildManager;
    }

    @EventHandler
    public void onPlayerMove(final @NonNull PlayerMoveEvent event) {

        final @NonNull Location from = event.getFrom();
        final @NonNull Location to = event.getTo();

        if (!(
                from.getBlockX() == to.getBlockX() &&
                from.getBlockY() == to.getBlockY() &&
                from.getBlockZ() == to.getBlockZ()
        )) {
            this.wildManager.invalidateWarmup(event.getPlayer());
        }

    }

}
