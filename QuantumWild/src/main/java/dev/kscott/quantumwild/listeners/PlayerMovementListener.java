package dev.kscott.quantumwild.listeners;

import com.google.inject.Inject;
import dev.kscott.quantumwild.wild.WildManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Listens on the {@link PlayerMoveEvent} to cancel warmups.
 */
public class PlayerMovementListener implements Listener {

    /**
     * {@link WildManager} reference.
     */
    private final @NonNull WildManager wildManager;

    /**
     * Constructs {@link PlayerMovementListener}.
     *
     * @param wildManager {@link WildManager} reference.
     */
    @Inject
    public PlayerMovementListener(final @NonNull WildManager wildManager) {
        this.wildManager = wildManager;
    }

    /**
     * If {@link PlayerMoveEvent#getFrom} and {@link PlayerMoveEvent#getTo} are in different blocks,
     * the player's warmup will be invalidated.
     *
     * @param event PlayerMoveEvent.
     */
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
