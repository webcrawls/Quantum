package dev.kscott.quantumwild.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

public class Lang {

    /**
     * JavaPlugin reference
     */
    private final @NonNull JavaPlugin plugin;

    private @NonNull ConfigurationNode root;

    /**
     * Constructs the lang.
     *
     * @param plugin {@link this#plugin}
     */
    public Lang(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;

        // Save config to file if it doesn't already exist
        if (!new File(this.plugin.getDataFolder(), "lang.conf").exists()) {
            plugin.saveResource("lang.conf", false);
        }

        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(Paths.get(plugin.getDataFolder().getAbsolutePath(), "lang.conf"))
                .build();

        try {
            root = loader.load();
        } catch (ConfigurateException e) {
            this.plugin.getLogger().severe("There was an error loading the lang: "+e.getMessage());
            this.plugin.getLogger().severe("QuantumWild will continue loading, but you should really fix this.");
            root = loader.createNode();
        }
    }


    /**
     * Gets the MiniMessage value at {@code key} and returns a parsed Component
     *
     * @param key path to value
     * @return Component
     */
    public @NonNull Component c(final @NonNull String key) {
        return c(key, Map.of());
    }

    /**
     * Gets the MiniMessage value at {@code key} and returns a parsed Component
     * All keys in the {@code replacements} param will be replaced with their value.
     *
     * @param key path to value
     * @param replacements a map where the key is a placeholder, and the value is what to replace that placeholder with
     * @return Component
     */
    public @NonNull Component c(final @NonNull String key, final @NonNull Map<String, String> replacements) {
        @Nullable String value = root.node((Object) key.split("\\.")).getString();

        if (value == null) {
            this.plugin.getLogger().severe("Tried to load lang key '" + key + "', but it didn't exist.");
            this.plugin.getLogger().severe("Using default value.");
            value = "<red>ERR</red>";
        }

        for (final Map.Entry<String, String> entry : replacements.entrySet()) {
            value = value.replace(entry.getKey(), entry.getValue());
        }

        return MiniMessage.get().parse(value);
    }

}
