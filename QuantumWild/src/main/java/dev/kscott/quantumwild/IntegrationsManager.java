package dev.kscott.quantumwild;

import com.earth2me.essentials.Essentials;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class IntegrationsManager {

    private final @NonNull JavaPlugin plugin;

    private @Nullable Essentials essentials;

    private @Nullable LuckPerms luckPerms;

    public IntegrationsManager(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;

        if (isLuckPermsEnabled()) {
            final @Nullable RegisteredServiceProvider<LuckPerms> rsp = plugin.getServer().getServicesManager().getRegistration(LuckPerms.class);

            if (rsp == null) {
                plugin.getLogger().warning("Features that depend on the LuckPerms API will not function correctly.");
                return;
            }

            luckPerms = rsp.getProvider();
        }

        if (isEssentialsEnabled()) {
            final @Nullable RegisteredServiceProvider<Essentials> rsp = plugin.getServer().getServicesManager().getRegistration(Essentials.class);

            if (rsp == null) {
                plugin.getLogger().warning("Features that depend on the Essentials API will not function correctly.");
                return;
            }

            essentials = rsp.getProvider();
        }
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public boolean isEssentialsEnabled() {
        return plugin.getServer().getPluginManager().isPluginEnabled("Essentials");
    }

    public boolean isLuckPermsEnabled() {
        return plugin.getServer().getPluginManager().isPluginEnabled("LuckPerms");
    }
}
