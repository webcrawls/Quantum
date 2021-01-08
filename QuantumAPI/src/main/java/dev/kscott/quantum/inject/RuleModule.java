package dev.kscott.quantum.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.rule.RuleRegistry;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class RuleModule extends AbstractModule {

    @Provides
    @Singleton
    public RulesetRegistry provideRulesetRegistry() {
        return new RulesetRegistry();
    }

    @Provides
    @Singleton
    public RuleRegistry provideRuleRegistry(
            final @NonNull JavaPlugin plugin
    ) {
        return new RuleRegistry(plugin);
    }

}
