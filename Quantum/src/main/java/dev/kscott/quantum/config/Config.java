package dev.kscott.quantum.config;

import com.google.inject.Inject;
import dev.kscott.quantum.rule.QuantumRule;
import dev.kscott.quantum.rule.RuleRegistry;
import dev.kscott.quantum.rule.option.QuantumRuleOption;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import dev.kscott.quantum.rule.ruleset.search.SearchArea;
import dev.kscott.quantum.rule.ruleset.target.HighestPossibleSpawnTarget;
import dev.kscott.quantum.rule.ruleset.target.LowestPossibleSpawnTarget;
import dev.kscott.quantum.rule.ruleset.target.RangeSpawnTarget;
import dev.kscott.quantum.rule.ruleset.target.SpawnTarget;
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

    public final Component PREFIX = MiniMessage.get().parse("<gray>[<color:#5bde9f>Quantum</color:#5bde9f>]<gray>");

    /**
     * JavaPlugin reference
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * RulesetRegistry reference
     */
    private final @NonNull RulesetRegistry rulesetRegistry;

    /**
     * RulesetRegistry reference
     */
    private final @NonNull RuleRegistry ruleRegistry;

    /**
     * The root quantum.conf config node
     */
    private @MonotonicNonNull CommentedConfigurationNode root;

    /**
     * Constructs the config, loads it, and loads rulesets.
     *
     * @param plugin          JavaPlugin reference
     * @param rulesetRegistry RulesetRegistry reference
     */
    @Inject
    public Config(final @NonNull JavaPlugin plugin, final @NonNull RulesetRegistry rulesetRegistry, final @NonNull RuleRegistry ruleRegistry) {
        this.plugin = plugin;
        this.rulesetRegistry = rulesetRegistry;
        this.ruleRegistry = ruleRegistry;

        // Save config to file if it doesn't already exist
        if (!new File(this.plugin.getDataFolder(), "config.conf").exists()) {
            plugin.saveResource("config.conf", false);
        }

        // Load the config
        this.loadConfig();

        // Load the rulesets (and register them with the provided RulesetRegistry)
        this.loadRulesets();
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

    /**
     * Loads the Rulesets into the {@link RulesetRegistry}
     */
    // TODO create a ruleset builder
    private void loadRulesets() {
        this.plugin.getLogger().info("Loading rulesets...");

        final @NonNull List<QuantumRuleset> rulesets = new ArrayList<>();

        final @NonNull ConfigurationNode rulesetsNode = root.node("rulesets");

        // Load rulesets
        for (final Map.Entry<Object, ? extends ConfigurationNode> entry : rulesetsNode.childrenMap().entrySet()) {
            Object key = entry.getKey();
            ConfigurationNode value = entry.getValue();

            if (!(key instanceof String)) {
                this.plugin.getLogger().severe("Error loading a ruleset (key was not a string): " + key.toString());
                continue;
            }

            String id = (String) key;

            // Load the world
            final @Nullable String worldName = value.node("world").getString();

            if (worldName == null) {
                this.plugin.getLogger().severe("Error loading ruleset " + id + " (world value didn't exist)");
                continue;
            }

            final @Nullable World world = this.plugin.getServer().getWorld(worldName);

            if (world == null) {
                this.plugin.getLogger().severe("Error loading ruleset " + id + " (world " + worldName + " was null)");
                continue;
            }

            final @NonNull UUID worldUuid = world.getUID();

            // Load spawn target
            final @Nullable String spawnTargetString = value.node("spawn-target").getString();

            if (spawnTargetString == null) {
                this.plugin.getLogger().severe("Error loading ruleset " + id + " (spawn-target was null)");
                continue;
            }

            @MonotonicNonNull SpawnTarget spawnTarget;

            if (spawnTargetString.equals("highest-possible")) {
                spawnTarget = new HighestPossibleSpawnTarget();
            } else if (spawnTargetString.equals("lowest-possible")) {
                spawnTarget = new LowestPossibleSpawnTarget();
            } else {
                // Attempt to parse spawn-target range value
                final @NonNull String[] slices = spawnTargetString.split("-");

                if (slices.length != 2) {
                    this.plugin.getLogger().severe("Error loading ruleset " + id + " (spawn-target range was null)");
                    continue;
                }

                final @NonNull String minString = slices[0];
                final @NonNull String maxString = slices[1];

                int min, max;

                try {
                    min = Integer.parseInt(minString);
                    max = Integer.parseInt(maxString);
                } catch (NumberFormatException e) {
                    this.plugin.getLogger().severe("Error loading ruleset " + id + " (spawn-target range was invalid - expected something like '30-70', got " + spawnTargetString + ")");
                    continue;
                }

                spawnTarget = new RangeSpawnTarget(min, max);
            }

            final @Nullable ConfigurationNode searchAreaNode = value.node("search-area");

            if (searchAreaNode == null) {
                this.plugin.getLogger().severe("Error loading ruleset " + id + " (search-area not found)");
                continue;
            }

            final int minX = searchAreaNode.node("min-x").getInt();
            final int maxX = searchAreaNode.node("max-x").getInt();
            final int minZ = searchAreaNode.node("min-z").getInt();
            final int maxZ = searchAreaNode.node("max-z").getInt();

            final @NonNull SearchArea searchArea = new SearchArea(minX, maxX, minZ, maxZ);

            final @NonNull List<QuantumRule> rules = new ArrayList<>();

            // TODO find some better way of doing this - configurate might have some mapper function
            for (final ConfigurationNode ruleConfig : value.node("rules").childrenList()) {

                final @Nullable String ruleType = ruleConfig.node("type").getString();

                if (ruleType == null) {
                    this.plugin.getLogger().severe("Error loading a rule (type was null)");
                    continue;
                }

                QuantumRule rule = ruleRegistry.createFreshRule(ruleType);

                if (rule == null) {
                    this.plugin.getLogger().severe("Error loading a rule (rule was null, '" + ruleType + "' was probably not registered)");
                    continue;
                }

                for (final Map.Entry<Object, ? extends ConfigurationNode> optionEntry : ruleConfig.node("options").childrenMap().entrySet()) {
                    Object optionKey = optionEntry.getKey();
                    ConfigurationNode optionValue = optionEntry.getValue();

                    if (!(optionKey instanceof String)) {
                        this.plugin.getLogger().severe("Error loading a ruleset (key was not a string): " + key.toString());
                        continue;
                    }

                    String optionId = (String) optionKey;

                    QuantumRuleOption<?> quantumRuleOption = rule.getOption(optionId);

                    try {
                        quantumRuleOption.setValue(optionValue.get(quantumRuleOption.getTypeToken().getType()));
                    } catch (SerializationException e) {
                        e.printStackTrace();
                    }

                    rules.add(rule);
                }
            }

            // add the ruleset to the list
            rulesets.add(new QuantumRuleset(id, worldUuid, spawnTarget, searchArea, rules));
        }

        for (final QuantumRuleset ruleset : rulesets) {
            rulesetRegistry.register(ruleset);
        }

        final @NonNull StringBuilder rulesetString = new StringBuilder("[");

        for (final QuantumRuleset ruleset : rulesets) {
            rulesetString
                    .append(ruleset.getId())
                    .append(ruleset == rulesets.get(rulesets.size() - 1) ? "" : ", ");
        }
        rulesetString.append("]");

        this.plugin.getLogger().info("Loaded " + rulesets.size() + " rulesets: " + rulesetString);
    }

}
