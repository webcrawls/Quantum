package dev.kscott.quantumwild.module;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import dev.kscott.quantumwild.wild.WildManager;
import net.luckperms.api.LuckPerms;
import org.checkerframework.checker.nullness.qual.NonNull;

public class WildModule extends AbstractModule {

    @Provides
    @Singleton
    @Inject
    public WildManager provideWildeManager(final @NonNull LuckPerms luckPerms) {
        return new WildManager(luckPerms);
    }

}
