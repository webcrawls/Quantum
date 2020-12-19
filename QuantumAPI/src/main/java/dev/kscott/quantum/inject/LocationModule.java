package dev.kscott.quantum.inject;

import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.config.Config;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.location.QuantumTimer;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public class LocationModule extends AbstractModule {
    @Singleton
    @Provides
    public @NonNull QuantumTimer provideQuantumTimer() {
        return new QuantumTimer();
    }

    @Singleton
    @Provides
    @Inject
    public @NonNull LocationProvider provideLocationProvider(
            final @NonNull Config config,
            final @NonNull QuantumTimer timer,
            final @NonNull PaperCommandManager<CommandSender> commandManager
    ) {
        return new LocationProvider(config, timer, commandManager);
    }

}
