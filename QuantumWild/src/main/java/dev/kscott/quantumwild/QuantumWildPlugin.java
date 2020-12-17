package dev.kscott.quantumwild;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.kscott.quantum.api.QuantumAPI;
import dev.kscott.quantumwild.module.QuantumModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class QuantumWildPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final @NonNull Injector injector = Guice.createInjector(
                new QuantumModule(this)
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
