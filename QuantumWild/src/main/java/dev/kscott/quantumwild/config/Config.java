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
import org.bukkit.Bukkit;
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
import java.util.*;

/**
 * Stores the Quantum configuration and handles the loading and registration of rulesets
 */
public class Config {

    /**
     * JavaPlugin reference
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * A map that stores a world's UUID and their associated ruleset
     */
    private final @NonNull Map<UUID, QuantumRuleset> worldRulesetMap;

    /**
     * The root quantum.conf config node
     */
    private @MonotonicNonNull CommentedConfigurationNode root;

    /**
     * RulesetRegistry reference
     */
    private final @NonNull RulesetRegistry rulesetRegistry;

    /**
     * Constructs the config, loads it, and loads rulesets.
     *
     * @param plugin          {@link this#plugin}
     * @param rulesetRegistry {@link this#rulesetRegistry}
     */
    public Config(final @NonNull RulesetRegistry rulesetRegistry, final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.rulesetRegistry = rulesetRegistry;

        // Save config to file if it doesn't already exist
        if (!new File(this.plugin.getDataFolder(), "config.conf").exists()) {
            plugin.saveResource("config.conf", false);
        }

        this.worldRulesetMap = new HashMap<>();

        // Load the config
        this.loadConfig();
        this.loadConfigurationValues();
    }

    /**
     * Returns a registered QuantumRuleset that is associated to a /wild world
     * @param world World
     * @return associated QuantumRuleset
     */
    public @Nullable QuantumRuleset getRuleset(final @NonNull World world) {
        return this.worldRulesetMap.get(world.getUID());
    }

    /**
     * Loads the config into the {@link this.root} node
     */
    private void loadConfig() {
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(Paths.get(plugin.getDataFolder().getAbsolutePath(), "config.conf"))
                .build();

        try {
            root = loader.load();
        } catch (ConfigurateException e) {
            throw new RuntimeException("Failed to load the configuration.", e);
        }
    }

    /**
     * Loads QuantumWild's configuration values
     */
    private void loadConfigurationValues() {
        this.worldRulesetMap.clear();

        for (final Map.Entry<Object, ? extends ConfigurationNode> entry :  root.node("worlds").childrenMap().entrySet()) {
            final @NonNull Object key = entry.getKey();

            if (!(key instanceof String)) {
                this.plugin.getLogger().severe("Error loading world ruleset map.");
                continue;
            }

            final @NonNull String worldName = (String) key;
            final @Nullable World world = Bukkit.getWorld(worldName);

            if (world == null) {
                this.plugin.getLogger().severe("Error loading ruleset map: world was null. Are you sure you spelled '"+worldName+"' correctly?");
                continue;
            }

            final @NonNull ConfigurationNode value = entry.getValue();

            final @Nullable String rulesetId = value.getString();

            if (rulesetId == null) {
                this.plugin.getLogger().severe("Error loading ruleset map: ruleset id was null.");
                continue;
            }

            final @Nullable QuantumRuleset ruleset = this.rulesetRegistry.getRuleset(rulesetId);

            if (ruleset == null) {
                this.plugin.getLogger().severe("Error loading ruleset map: RulesetRegistry returned null. Are you sure you spelled '"+rulesetId+"' correctly?");
                continue;
            }

            this.worldRulesetMap.put(world.getUID(), ruleset);
        }
    }
}
