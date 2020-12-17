package dev.kscott.quantum.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.location.LocationProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class LocationModule extends AbstractModule {

    private final @NonNull JavaPlugin plugin;

    public LocationModule(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Singleton
    @Provides
    public @NonNull LocationProvider provideLocationProvider() {
        return new LocationProvider();
    }

}
