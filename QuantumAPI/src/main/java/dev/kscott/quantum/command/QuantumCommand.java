package dev.kscott.quantum.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.Description;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import dev.kscott.quantum.config.Config;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.location.QuantumLocation;
import dev.kscott.quantum.location.QuantumTimer;
import dev.kscott.quantum.rule.RuleRegistry;
import dev.kscott.quantum.rule.rules.async.AsyncQuantumRule;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * The base /quantum command.
 */
public class QuantumCommand {

    /**
     * CommandManager reference.
     */
    private final @NonNull CommandManager<CommandSender> commandManager;

    /**
     * RulesetRegistry reference.
     */
    private final @NonNull RulesetRegistry rulesetRegistry;

    /**
     * RuleRegistry reference.
     */
    private final @NonNull RuleRegistry ruleRegistry;

    /**
     * JavaPlugin reference.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * Config reference.
     */
    private final @NonNull Config config;

    /**
     * BukkitAudiences reference.
     */
    private final @NonNull BukkitAudiences bukkitAudiences;

    /**
     * QuantumTimer reference.
     */
    private final @NonNull QuantumTimer timer;

    /**
     * LocationProvider reference.
     */
    private final @NonNull LocationProvider locationProvider;

    /**
     * Stores a list of all registered ruleset ids for command completion.
     */
    private final @NonNull List<String> rulesetIds;

    /**
     * Constructs QuantumCommand.
     *
     * @param plugin           JavaPlugin reference.
     * @param ruleRegistry     RuleRegistry reference.
     * @param timer            QuantumTimer reference.
     * @param commandManager   CommandManager reference.
     * @param bukkitAudiences  BukkitAudiences reference.
     * @param config           Config reference.
     * @param rulesetRegistry  RulesetRegistry reference.
     * @param locationProvider LocationProvider reference.
     */
    @Inject
    public QuantumCommand(
            final @NonNull JavaPlugin plugin,
            final @NonNull QuantumTimer timer,
            final @NonNull RuleRegistry ruleRegistry,
            final @NonNull BukkitAudiences bukkitAudiences,
            final @NonNull Config config,
            final @NonNull CommandManager<CommandSender> commandManager,
            final @NonNull RulesetRegistry rulesetRegistry,
            final @NonNull LocationProvider locationProvider
    ) {
        this.plugin = plugin;
        this.commandManager = commandManager;
        this.rulesetRegistry = rulesetRegistry;
        this.config = config;
        this.bukkitAudiences = bukkitAudiences;
        this.ruleRegistry = ruleRegistry;
        this.timer = timer;
        this.locationProvider = locationProvider;
        this.rulesetIds = this.rulesetRegistry.getRulesets().stream().map(QuantumRuleset::getId).collect(Collectors.toList());
        setupCommands();
    }

    /**
     * Sets up commands.
     */
    private void setupCommands() {
        final Command.Builder<CommandSender> builder = this.commandManager.commandBuilder("quantum", "q");

        this.commandManager.command(
                builder.handler(this::handleMain)
        );

        this.commandManager.command(
                builder.literal(
                        "rulesets",
                        ArgumentDescription.of("Get the ids of all loaded rulesets")
                )
                        .permission("quantum.api.command.rulesets")
                        .handler(this::handleRulesets)
        );

        this.commandManager.command(
                builder.literal(
                        "rules",
                        ArgumentDescription.of("Get the ids all loaded rules")
                )
                        .permission("quantum.api.command.rules")
                        .handler(this::handleRules)
        );

        this.commandManager.command(
                builder.literal(
                        "stats",
                        ArgumentDescription.of("See Quantum's performance")
                )
                        .permission("quantum.api.command.stats")
                        .handler(this::handleStats)
        );

        this.commandManager.command(
                builder.literal(
                        "reload",
                        ArgumentDescription.of("Reloads Quantum reload")
                )
                        .permission("quantum.api.command.reload")
                        .handler(this::handleReload)
        );

        this.commandManager.command(
                builder.literal(
                        "queue",
                        ArgumentDescription.of("Shows QuantumAPI queue stats")
                )
                        .permission("quantum.api.command.queue")
                        .handler(this::handleQueue)
        );

        final CommandArgument<CommandSender, String> rulesetArg = StringArgument.<CommandSender>newBuilder("ruleset")
                .asOptional()
                .withSuggestionsProvider((ctx, arg) -> rulesetIds)
                .build();


        this.commandManager.command(
                builder.literal(
                        "validate",
                        ArgumentDescription.of("Validates a location with a given ruleset")
                )
                        .permission("quantum.api.command.validate")
                        .argument(rulesetArg)
                        .handler(this::handleValidate)
        );
    }

    /**
     * Handles /quantum reload.
     *
     * @param context command context.
     */
    private void handleReload(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();

        config.reload();

        final TextComponent.Builder component = Component.text()
                .append(this.config.PREFIX)
                .append(MiniMessage.get().parse(" <gray>Reloaded all rulesets!</gray>"));

        bukkitAudiences.sender(sender).sendMessage(component);
    }

    /**
     * Handles /quantum.
     *
     * @param context command context.
     */
    private void handleMain(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();

        final @NonNull String version = this.plugin.getDescription().getVersion();

        final TextComponent.Builder component = Component.text()
                .append(this.config.PREFIX)
                .append(MiniMessage.get().parse(" <gray>Quantum v<aqua>" + version + "</aqua></gray>"))
                .append(MiniMessage.get().parse(" <gray>Join the support Discord @ <aqua>chat.ksc.sh</aqua>!</gray>"));

        bukkitAudiences.sender(sender).sendMessage(component);
    }

    /**
     * Handles /quantum rulesets.
     *
     * @param context command context.
     */
    private void handleRulesets(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();

        final @NonNull List<QuantumRuleset> rulesets = new ArrayList<>(rulesetRegistry.getRulesets());

        final TextComponent.Builder component = Component.text()
                .append(this.config.PREFIX)
                .append(MiniMessage.get().parse(" <gray>There " + (rulesets.size() == 1 ? "is" : "are") + " currently <aqua>" + rulesets.size() + "</aqua> registered ruleset" + (rulesets.size() == 1 ? "" : "s") + ": "));

        for (int i = 0; i < rulesets.size(); i++) {
            boolean isLast = i + 1 == rulesets.size();
            final @NonNull QuantumRuleset ruleset = rulesets.get(i);
            component.append(MiniMessage.get().parse("<aqua>" + ruleset.getId() + "<aqua>" + (isLast ? "" : ", ")));
        }

        this.bukkitAudiences.sender(sender).sendMessage(Identity.nil(), component);
    }

    /**
     * Handles /quantum rules.
     *
     * @param context command context.
     */
    private void handleRules(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();

        final @NonNull List<RuleRegistry.EffectiveRule> rules = new ArrayList<>(ruleRegistry.getRules());

        final TextComponent.Builder component = Component.text()
                .append(this.config.PREFIX)
                .append(MiniMessage.get().parse(" <gray>There " + (rules.size() == 1 ? "is" : "are") + " currently <aqua>" + rules.size() + "</aqua> registered rules" + (rules.size() == 1 ? "" : "s") + ": "));

        for (int i = 0; i < rules.size(); i++) {
            boolean isLast = i + 1 == rules.size();
            final RuleRegistry.EffectiveRule rule = rules.get(i);

            final boolean isAsync = AsyncQuantumRule.class.isAssignableFrom(rule.getRuleClass());

            component.append(MiniMessage.get().parse("<aqua>" + rule.getId() + " <dark_aqua>" + (isAsync ? "async" : "sync") + "</dark_aqua></aqua>" + (isLast ? "" : ", ")));
        }

        this.bukkitAudiences.sender(sender).sendMessage(Identity.nil(), component);
    }

    /**
     * Handles /quantum stats.
     *
     * @param context command context.
     */
    private void handleStats(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();

        final @NonNull Audience audience = this.bukkitAudiences.sender(sender);

        int searches = this.timer.getTotalSearches();

        if (searches == 0) {
            final TextComponent.Builder component = Component.text()
                    .append(this.config.PREFIX)
                    .append(MiniMessage.get().parse(" <gray>Quantum has not generated any locations yet."));

            audience.sendMessage(component);
            return;
        }


        final TextComponent.Builder searchesComponent = Component.text()
                .append(this.config.PREFIX)
                .append(MiniMessage.get().parse(" <gray>Quantum has generated <aqua>" + searches + " location" + (searches == 1 ? "" : "s") + "</aqua>."));
        audience.sendMessage(searchesComponent);

        final double seconds = this.timer.getTotalTime() / 1000.0;
        final double average = this.timer.getAverageTime() / 1000.0;

        final TextComponent.Builder timeComponent = Component.text()
                .append(this.config.PREFIX)
                .append(MiniMessage.get().parse(" <gray>On average, Quantum spends <aqua>" + average + " seconds</aqua> searching for a location."))
                .append(MiniMessage.get().parse(" <gray>In total, Quantum has spent <aqua>" + seconds + " seconds</aqua> searching for locations."));
        audience.sendMessage(timeComponent);
    }

    /**
     * Handles /quantum queue.
     *
     * @param context command context.
     */
    private void handleQueue(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();

        final @NonNull Audience audience = this.bukkitAudiences.sender(sender);

        final @NonNull Map<QuantumRuleset, Queue<QuantumLocation>> locationMap = this.locationProvider.getQueuedLocationMap();

        for (final Map.Entry<QuantumRuleset, Queue<QuantumLocation>> locationEntry : locationMap.entrySet()) {
            final TextComponent.Builder component = Component.text()
                    .append(this.config.PREFIX)
                    .append(MiniMessage.get().parse(" <aqua>" + locationEntry.getKey().getId() + "</aqua> <gray>has <aqua>" + locationEntry.getValue().size() + "</aqua> queued locations.</gray>"));

            audience.sendMessage(component);
        }
    }

    /**
     * Handles /quantum validate.
     *
     * @param context command context.
     */
    private void handleValidate(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();
        final @NonNull Audience audience = this.bukkitAudiences.sender(sender);

        if (!(sender instanceof Player)) {
            final TextComponent.Builder component = Component.text()
                    .append(this.config.PREFIX)
                    .append(MiniMessage.get().parse(" <red>Only players can execute this command.</red>"));
            audience.sendMessage(component);
            return;
        }

        final @NonNull Player player = (Player) sender;

        final @NonNull String rulesetId = context.get("ruleset");
        final @Nullable QuantumRuleset ruleset = this.rulesetRegistry.getRuleset(rulesetId);

        if (ruleset == null) {
            final TextComponent.Builder component = Component.text()
                    .append(this.config.PREFIX)
                    .append(MiniMessage.get().parse(" <red>There is no ruleset with the id <yellow>" + rulesetId + "</yellow></red>"));
            audience.sendMessage(component);
            return;
        }

        final @NonNull Location location = player.getLocation();

        this.locationProvider.validateLocation(location, ruleset)
                .thenAccept(valid -> {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            final TextComponent.Builder component = Component.text()
                                    .append(config.PREFIX)
                                    .append(MiniMessage.get().parse(valid ? " <aqua>This location is valid.</aqua>" : " <red>This location is not valid.</red>"));
                            audience.sendMessage(component);
                        }
                    }.runTask(plugin);
                });

    }

}
