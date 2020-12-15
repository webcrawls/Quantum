package dev.kscott.quantum.location;

import com.google.inject.Inject;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The core LocationProvider. Generates spawn points, handles spawn rules, etc
 */
public class LocationProvider {

    private final @NonNull JavaPlugin plugin;

    @Inject
    public LocationProvider(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

}
