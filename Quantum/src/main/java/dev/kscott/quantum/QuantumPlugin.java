package dev.kscott.quantum;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import dev.kscott.quantum.api.QuantumAPI;
import dev.kscott.quantum.command.QuantumCommand;
import dev.kscott.quantum.config.Config;
import dev.kscott.quantum.inject.*;
import org.bukkit.plugin.ServicePriority;
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
                new LocationModule(),
                new RuleModule(),
                new ConfigModule()
        );


        this.loadConfig(injector);

        injector.getInstance(QuantumCommand.class);

        final @NonNull QuantumAPI quantumAPI = injector.getInstance(QuantumAPI.class);

        this.getServer().getServicesManager().register(QuantumAPI.class, quantumAPI, this, ServicePriority.High);
    }

    private void loadConfig(final @NonNull Injector injector) {
        final @NonNull Config config = injector.getInstance(Config.class);
    }
}
