package dev.kscott.quantumwild.module;

import com.earth2me.essentials.Essentials;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantumwild.IntegrationsManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Provides integrations with various third-party plugin APIs.
 */
public class IntegrationsModule extends AbstractModule {

    /**
     * Provides the IntegrationsManager
     *
     * @param plugin JavaPlugin instance
     * @return LuckPerms, may be null
     */
    @Provides
    @Singleton
    @Inject
    public @Nullable IntegrationsManager provideIntegrationsManage(final @NonNull JavaPlugin plugin) {
        return new IntegrationsManager(plugin);
    }

}
