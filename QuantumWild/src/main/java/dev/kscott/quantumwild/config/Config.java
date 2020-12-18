package dev.kscott.quantumwild.config;

import com.google.inject.Inject;
import dev.kscott.quantum.location.locator.HighestPossibleYLocator;
import dev.kscott.quantum.location.locator.LowestPossibleYLocator;
import dev.kscott.quantum.location.locator.RangeYLocator;
import dev.kscott.quantum.location.locator.YLocator;
import dev.kscott.quantum.rule.QuantumRule;
import dev.kscott.quantum.rule.RuleRegistry;
import dev.kscott.quantum.rule.option.QuantumRuleOption;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import dev.kscott.quantum.rule.ruleset.search.SearchArea;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Stores the Quantum configuration and handles the loading and registration of rulesets
 */
public class Config {

    /**
     * JavaPlugin reference
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * The root quantum.conf config node
     */
    private @MonotonicNonNull CommentedConfigurationNode root;

    /**
     * Constructs the config, loads it, and loads rulesets.
     *
     * @param plugin          JavaPlugin reference
     */
    @Inject
    public Config(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;

        // Save config to file if it doesn't already exist
        if (!new File(this.plugin.getDataFolder(), "config.conf").exists()) {
            plugin.saveResource("config.conf", false);
        }

        // Load the config
        this.loadConfig();

    }

    /**
     * Loads the config into the {@link this.root} node
     */
    private void loadConfig() {
        this.plugin.getLogger().info("Loading config.conf...");
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(Paths.get(plugin.getDataFolder().getAbsolutePath(), "config.conf"))
                .build();

        try {
            root = loader.load();
        } catch (ConfigurateException e) {
            throw new RuntimeException("Failed to load the configuration.", e);
        }
    }
}
