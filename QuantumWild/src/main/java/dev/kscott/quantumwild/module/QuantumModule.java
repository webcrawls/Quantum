package dev.kscott.quantumwild.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.api.QuantumAPI;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.rule.RuleRegistry;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Exposes methods to interact with the {@link QuantumAPI}.
 */
public class QuantumModule extends AbstractModule {

    /**
     * QuantumAPI reference.
     */
    private final @MonotonicNonNull QuantumAPI quantumAPI;

    /**
     * Constructs {@link QuantumModule}, and loads {@link QuantumAPI}.
     *
     * @param plugin {@link JavaPlugin} reference.
     */
    public QuantumModule(final @NonNull JavaPlugin plugin) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("QuantumAPI")) {
            throw new RuntimeException("Quantum was not found! Please ensure it is present, as QuantumWild requires it to function.");
        }

        this.quantumAPI = plugin.getServer().getServicesManager().load(QuantumAPI.class);

        if (this.quantumAPI == null) {
            throw new RuntimeException("Could not load the QuantumAPI! Please contact bluely with this error.");
        }
    }

    /**
     * Provides {@link LocationProvider}.
     *
     * @return {@link LocationProvider} reference.
     */
    @Provides
    @Singleton
    public @NonNull LocationProvider provideLocationProvider() {
        return this.quantumAPI.getLocationProvider();
    }

    /**
     * Provides {@link RulesetRegistry}.
     *
     * @return {@link RulesetRegistry} reference.
     */
    @Provides
    @Singleton
    public @NonNull RulesetRegistry provideRulesetRegistry() {
        return this.quantumAPI.getRulesetRegistry();
    }

    /**
     * Provides {@link RulesetRegistry}.
     *
     * @return {@link RuleRegistry} reference.
     */
    @Provides
    @Singleton
    public @NonNull RuleRegistry provideRuleRegistry() {
        return this.quantumAPI.getRuleRegistry();
    }

}
