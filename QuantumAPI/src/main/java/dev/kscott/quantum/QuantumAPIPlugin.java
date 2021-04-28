package dev.kscott.quantum;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import dev.kscott.quantum.api.QuantumAPI;
import dev.kscott.quantum.command.QuantumCommand;
import dev.kscott.quantum.config.Config;
import dev.kscott.quantum.inject.CommandModule;
import dev.kscott.quantum.inject.ConfigModule;
import dev.kscott.quantum.inject.LocationModule;
import dev.kscott.quantum.inject.PluginModule;
import dev.kscott.quantum.inject.RuleModule;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The QuantumAPIPlugin. Loads the QuantumAPI.
 */
@Singleton
public final class QuantumAPIPlugin extends JavaPlugin {

    /**
     * Registers modules, loads config, and exposes QuantumAPI.
     */
    @Override
    public void onEnable() {
        final @NonNull Injector injector = Guice.createInjector(
                new PluginModule(this),
                new CommandModule(this),
                new RuleModule(),
                new ConfigModule(),
                new LocationModule()
        );

        this.loadConfig(injector);

        injector.getInstance(QuantumCommand.class);

        this.getServer().getServicesManager().register(
                QuantumAPI.class,
                injector.getInstance(QuantumAPI.class),
                this,
                ServicePriority.High
        );

        new Metrics(this, 9725);
    }

    /**
     * Loads the Config through guice
     *
     * @param injector Guice injector instance
     */
    private void loadConfig(final @NonNull Injector injector) {
        injector.getInstance(Config.class);
    }
}
