package dev.kscott.quantum.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.config.Config;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ConfigModule extends AbstractModule {

    @Provides
    @Singleton
    @Inject
    public Config provideConfig(final @NonNull JavaPlugin plugin, final @NonNull RulesetRegistry rulesetRegistry) {
        return new Config(plugin, rulesetRegistry);
    }

}
