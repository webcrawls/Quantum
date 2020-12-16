package dev.kscott.quantumspawn;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import dev.kscott.quantumwild.command.QuantumCommand;
import dev.kscott.quantumwild.inject.CommandModule;
import dev.kscott.quantumwild.inject.LocationModule;
import dev.kscott.quantumwild.inject.PluginModule;
import dev.kscott.quantumwild.location.LocationProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class QuantumSpawnPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        final @NonNull Injector injector = Guice.createInjector(
                new PluginModule(this),
                new CommandModule(this),
                new LocationModule(this)
        );

        injector.getInstance(QuantumCommand.class);

        final @NonNull LocationProvider locationProvider = injector.getInstance(LocationProvider.class);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
