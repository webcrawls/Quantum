package dev.kscott.quantum.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.rule.RuleRegistry;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;

public class RuleModule extends AbstractModule {

    @Provides
    @Singleton
    public RulesetRegistry provideRulesetRegistry() {
        return new RulesetRegistry();
    }

    @Provides
    @Singleton
    public RuleRegistry provideRuleRegistry() {
        return new RuleRegistry();
    }

}
