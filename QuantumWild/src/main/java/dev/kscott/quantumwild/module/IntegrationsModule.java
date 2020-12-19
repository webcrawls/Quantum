package dev.kscott.quantumwild.module;

import com.earth2me.essentials.Essentials;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class IntegrationsModule extends AbstractModule {

    @Provides
    @Singleton
    @Inject
    public @Nullable LuckPerms providePermissionApi(final @NonNull JavaPlugin plugin) {
        final @Nullable RegisteredServiceProvider<LuckPerms> rsp = plugin.getServer().getServicesManager().getRegistration(LuckPerms.class);

        if (rsp == null) {
            plugin.getLogger().warning("Features that depend on the LuckPerms API will not function correctly.");
            return null;
        }

        return rsp.getProvider();
    }

    @Provides
    @Singleton
    @Inject
    public @Nullable Essentials provideEssentialsApi(final @NonNull JavaPlugin plugin) {
        final @Nullable RegisteredServiceProvider<Essentials> rsp = plugin.getServer().getServicesManager().getRegistration(Essentials.class);

        if (rsp == null) {
            plugin.getLogger().warning("Features that depend on the EssentialsX API will not function correctly.");
            return null;
        }

        return rsp.getProvider();
    }

}
