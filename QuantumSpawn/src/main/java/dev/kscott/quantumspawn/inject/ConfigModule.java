package dev.kscott.quantumspawn.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import dev.kscott.quantumspawn.config.Config;
import dev.kscott.quantumspawn.config.Lang;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Provides the configuration and lang objects
 */
public class ConfigModule extends AbstractModule {

    /**
     * Provides {@link Config}.
     *
     * @param rulesetRegistry RulesetRegistry reference.
     * @param plugin JavaPlugin reference.
     * @return Config.
     */
    @Provides
    @Singleton
    @Inject
    public @NonNull Config provideConfig(final @NonNull RulesetRegistry rulesetRegistry, final @NonNull JavaPlugin plugin) {
        return new Config(rulesetRegistry, plugin);
    }

    /**
     * Provides {@link Lang}
     *
     * @param plugin JavaPlugin reference.
     * @return Lang.
     */
    @Provides
    @Singleton
    @Inject
    public @NonNull Lang provideLang(final @NonNull JavaPlugin plugin) {
        return new Lang(plugin);
    }

}
