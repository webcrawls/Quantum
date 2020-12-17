package dev.kscott.quantumwild.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.api.QuantumAPI;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.rule.RuleRegistry;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The module that interacts with the QuantumAPI
 */
public class QuantumModule extends AbstractModule {

    private final @NonNull QuantumAPI quantumAPI;

    public QuantumModule(final @NonNull JavaPlugin plugin) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("Quantum")) {
            throw new RuntimeException("Quantum was not found! Please ensure it is present, as QuantumWild requires it to function.");
        }

        this.quantumAPI = plugin.getServer().getServicesManager().load(QuantumAPI.class);

        if (this.quantumAPI == null) {
            throw new RuntimeException("Could not load the QuantumAPI! Please contact bluely with this error.");
        }
    }

    @Provides
    @Singleton
    public @NonNull LocationProvider provideLocationProvider() {
        return this.quantumAPI.getLocationProvider();
    }

    @Provides
    @Singleton
    public @NonNull RulesetRegistry provideRulesetRegistry() {
        return this.quantumAPI.getRulesetRegistry();
    }

    @Provides
    @Singleton
    public @NonNull RuleRegistry provideRuleRegistry() {
        return this.quantumAPI.getRuleRegistry();
    }

}
