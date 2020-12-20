package dev.kscott.quantumwild;

import com.earth2me.essentials.Essentials;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Manages integrations with other plugins.
 */
public class IntegrationsManager {

    /**
     * JavaPlugin reference
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * Essentials reference
     */
    private @Nullable Essentials essentials;

    /**
     * LuckPerms reference
     */
    private @Nullable LuckPerms luckPerms;

    /**
     * Constructs IntegrationsManager
     *
     * @param plugin {@link this#plugin}
     */
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

    /**
     * Returns {@link Essentials} if it is enabled
     *
     * @return Essentials instance, may be null if {@link this#isEssentialsEnabled()} is false
     */
    public @Nullable Essentials getEssentials() {
        return essentials;
    }

    /**
     * Returns {@link LuckPerms} if it is enabled
     *
     * @return Essentials instance, may be null if {@link this#isLuckPermsEnabled()} is false
     */
    public @Nullable LuckPerms getLuckPerms() {
        return luckPerms;
    }

    /**
     * Checks if EssentialsX is enabled
     *
     * @return true if enabled, false if not
     */
    public boolean isEssentialsEnabled() {
        return plugin.getServer().getPluginManager().getPlugin("Essentials") != null;
    }

    /**
     * Checks if LuckPerms is enabled
     *
     * @return true if enabled, false if not
     */
    public boolean isLuckPermsEnabled() {
        return plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null;
    }
}
