package dev.kscott.quantumspawn;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.kscott.quantumspawn.config.Config;
import dev.kscott.quantumspawn.listeners.PlayerJoinListener;
import dev.kscott.quantumspawn.module.ConfigModule;
import dev.kscott.quantumspawn.module.QuantumModule;
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
                new QuantumModule(this),
                new ConfigModule()
        );

        final @NonNull Config config = loadConfig(injector);

        if (config.isSpawnOnJoinEnabled()) {
            final @NonNull PlayerJoinListener playerJoinListener = injector.getInstance(PlayerJoinListener.class);
            this.getServer().getPluginManager().registerEvents(playerJoinListener, this);
        }


        new Metrics(this, 9727);
    }

    private @NonNull Config loadConfig(final @NonNull Injector injector) {
        return injector.getInstance(Config.class);
    }
}
