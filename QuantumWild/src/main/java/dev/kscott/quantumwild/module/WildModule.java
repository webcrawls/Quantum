package dev.kscott.quantumwild.module;

import com.earth2me.essentials.Essentials;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantumwild.config.Config;
import dev.kscott.quantumwild.config.Lang;
import dev.kscott.quantumwild.wild.WildManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class WildModule extends AbstractModule {

    @Provides
    @Singleton
    @Inject
    public WildManager provideWildeManager(
            final @NonNull Essentials essentials,
            final @NonNull Lang lang,
            final @NonNull BukkitAudiences audiences,
            final @NonNull Config config,
            final @NonNull LocationProvider locationProvider,
            final @NonNull JavaPlugin plugin,
            final @NonNull LuckPerms luckPerms
    ) {
        return new WildManager(luckPerms, essentials, lang, audiences, config, locationProvider, plugin);
    }

}
