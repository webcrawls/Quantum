package dev.kscott.quantum.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;

public class RulesetModule extends AbstractModule {

    @Provides
    @Singleton
    public RulesetRegistry provideRulesetRegistry() {
        return new RulesetRegistry();
    }

}
