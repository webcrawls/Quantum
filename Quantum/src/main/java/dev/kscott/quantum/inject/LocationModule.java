package dev.kscott.quantum.inject;

import cloud.commandframework.CommandManager;
import cloud.commandframework.paper.PaperCommandManager;
import cloud.commandframework.tasks.TaskRecipe;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.location.LocationProvider;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public class LocationModule extends AbstractModule {
    @Singleton
    @Provides
    @Inject
    public @NonNull LocationProvider provideLocationProvider(final @NonNull PaperCommandManager<CommandSender> commandManager) {
        return new LocationProvider(commandManager);
    }

}
