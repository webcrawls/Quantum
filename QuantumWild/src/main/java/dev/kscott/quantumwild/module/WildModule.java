package dev.kscott.quantumwild.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantumwild.wild.WildManager;

public class WildModule extends AbstractModule {

    @Provides
    @Singleton
    public WildManager provideWildeManager() {
        return new WildManager();
    }

}
