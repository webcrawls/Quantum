package dev.kscott.quantum.inject;

import com.google.inject.AbstractModule;
import dev.kscott.quantum.QuantumAPIPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The guice module for
 */
public class PluginModule extends AbstractModule {
    /**
     * The PlayerProfilesPlugin reference
     */
    private final @NonNull QuantumAPIPlugin plugin;

    /**
     * The Audiences instance
     */
    private final @NonNull BukkitAudiences audiences;

    /**
     * Constructs PluginModule
     *
     * @param plugin PlayerProfilesPlugin reference
     */
    public PluginModule(final @NonNull QuantumAPIPlugin plugin) {
        this.plugin = plugin;
        this.audiences = BukkitAudiences.create(plugin);
    }

    /**
     * Configures the Guice module
     */
    @Override
    public void configure() {
        this.bind(Plugin.class).toInstance(this.plugin);
        this.bind(JavaPlugin.class).toInstance(this.plugin);
        this.bind(QuantumAPIPlugin.class).toInstance(this.plugin);
        this.bind(BukkitAudiences.class).toInstance(this.audiences);
    }

}
