package dev.kscott.quantum;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import dev.kscott.quantum.inject.CommandModule;
import dev.kscott.quantum.inject.LocationModule;
import dev.kscott.quantum.inject.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

@Singleton
public final class QuantumPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        final @NonNull Injector injector = Guice.createInjector(
                new PluginModule(this),
                new CommandModule(this),
                new LocationModule(this)
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
