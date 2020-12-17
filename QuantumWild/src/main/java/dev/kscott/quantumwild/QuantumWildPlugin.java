package dev.kscott.quantumwild;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.kscott.quantumwild.module.CommandModule;
import dev.kscott.quantumwild.module.PluginModule;
import dev.kscott.quantumwild.module.QuantumModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class QuantumWildPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final @NonNull Injector injector = Guice.createInjector(
                new QuantumModule(this),
                new PluginModule(this),
                new CommandModule(this)
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
