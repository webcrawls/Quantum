package dev.kscott.quantumspawn;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.kscott.quantumspawn.config.Config;
import dev.kscott.quantumspawn.inject.ConfigModule;
import dev.kscott.quantumspawn.inject.PluginModule;
import dev.kscott.quantumspawn.inject.QuantumModule;
import dev.kscott.quantumspawn.listeners.PlayerDeathListener;
import dev.kscott.quantumspawn.listeners.PlayerFirstJoinListener;
import dev.kscott.quantumspawn.listeners.PlayerJoinListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The QuantumSpawnPlugin.
 */
public final class QuantumSpawnPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final @NonNull Injector injector = Guice.createInjector(
                new PluginModule(this),
                new QuantumModule(this),
                new ConfigModule()
        );

        final @NonNull Config config = loadConfig(injector);

        if (config.isSpawnOnJoinEnabled()) {
            this.getServer().getPluginManager().registerEvents(injector.getInstance(PlayerJoinListener.class), this);
            this.getLogger().info("Random spawn on join is enabled!");
        }

        if (config.isSpawnOnFirstJoinEnabled()) {
            this.getServer().getPluginManager().registerEvents(injector.getInstance(PlayerFirstJoinListener.class), this);
            this.getLogger().info("Random spawn on first join is enabled!");
        }

        if (config.isSpawnOnDeathEnabled()) {
            this.getServer().getPluginManager().registerEvents(injector.getInstance(PlayerDeathListener.class), this);
            this.getLogger().info("Random spawn on death is enabled!");
        }


        new Metrics(this, 9727);
    }

    private @NonNull Config loadConfig(final @NonNull Injector injector) {
        return injector.getInstance(Config.class);
    }
}
