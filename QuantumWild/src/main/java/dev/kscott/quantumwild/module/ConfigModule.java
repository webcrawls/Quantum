package dev.kscott.quantumwild.module;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantumwild.config.Config;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ConfigModule extends AbstractModule {

    @Provides
    @Singleton
    @Inject
    public Config provideConfig(final @NonNull JavaPlugin plugin) {
        return new Config(plugin);
    }

}
