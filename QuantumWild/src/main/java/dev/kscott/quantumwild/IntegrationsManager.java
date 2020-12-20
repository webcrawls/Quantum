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
            } else {
                luckPerms = rsp.getProvider();
            }

        }

        if (isEssentialsEnabled()) {
            essentials = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
        }
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public boolean isEssentialsEnabled() {
        return plugin.getServer().getPluginManager().getPlugin("Essentials") != null;
    }

    public boolean isLuckPermsEnabled() {
        return plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null;
    }
}
