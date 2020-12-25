package dev.kscott.quantumwild.module;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantumwild.IntegrationsManager;
import dev.kscott.quantumwild.config.Config;
import dev.kscott.quantumwild.config.Lang;
import dev.kscott.quantumwild.wild.WildManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Provides the WildManager
 */
public class WildModule extends AbstractModule {

    /**
     * Constructs & returns the WildManager
     *
     * @param integrationsManager IntegrationsManager reference.
     * @param lang Lang reference.
     * @param audiences BukkitAudiences reference.
     * @param config Config reference.
     * @param locationProvider LocationProvider reference.
     * @param plugin JavaPlugin reference.
     * @return
     */
    @Provides
    @Singleton
    @Inject
    public WildManager provideWildManager(
            final @NonNull IntegrationsManager integrationsManager,
            final @NonNull Lang lang,
            final @NonNull BukkitAudiences audiences,
            final @NonNull Config config,
            final @NonNull LocationProvider locationProvider,
            final @NonNull JavaPlugin plugin
    ) {
        return new WildManager(integrationsManager, lang, audiences, config, locationProvider, plugin);
    }

}
