package dev.kscott.quantum.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.Description;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import dev.kscott.quantum.config.Config;
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
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The base /quantum command.
 */
public class QuantumCommand {

    /**
     * CommandManager reference
     */
    private final @NonNull CommandManager<CommandSender> commandManager;

    /**
     * RulesetRegistry reference
     */
    private final @NonNull RulesetRegistry rulesetRegistry;

    /**
     * RuleRegistry reference
     */
    private final @NonNull RuleRegistry ruleRegistry;

    /**
     * Config reference
     */
    private final @NonNull Config config;

    /**
     * BukkitAudiences reference
     */
    private final @NonNull BukkitAudiences bukkitAudiences;

    private final @NonNull QuantumTimer timer;

    /**
     * Constructs QuantumCommand
     *
     * @param commandManager  CommandManager reference
     * @param bukkitAudiences BukkitAudiences reference
     * @param config          Config reference
     * @param rulesetRegistry RulesetRegistry reference
     */
    @Inject
    public QuantumCommand(final @NonNull QuantumTimer timer, final @NonNull RuleRegistry ruleRegistry, final @NonNull BukkitAudiences bukkitAudiences, final @NonNull Config config, final @NonNull CommandManager<CommandSender> commandManager, final @NonNull RulesetRegistry rulesetRegistry) {
        this.commandManager = commandManager;
        this.rulesetRegistry = rulesetRegistry;
        this.config = config;
        this.bukkitAudiences = bukkitAudiences;
        this.ruleRegistry = ruleRegistry;
        this.timer = timer;
        setupCommands();
    }

    /**
     * Sets up commands.
     */
    private void setupCommands() {
        final Command.Builder<CommandSender> builder = this.commandManager.commandBuilder("quantum", "q");

        this.commandManager.command(
                builder.literal(
                        "rulesets",
                        Description.of("Get the ids of all loaded rulesets")
                )
                        .permission("quantum.api.command.rulesets")
                        .handler(this::handleRulesets)
        );

        this.commandManager.command
                (builder.literal(
                        "rules",
                        Description.of("Get the ids all loaded rules")
                )
                        .permission("quantum.api.command.rules")
                        .handler(this::handleRules)
        );

        this.commandManager.command(
                builder.literal(
                        "stats",
                        Description.of("See Quantum's performance")
                )
                        .permission("quantum.api.command.stats")
                        .handler(this::handleStats)
        );
    }

    /**
     * Handles /quantum rulesets
     *
     * @param context command context
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
     * Handles /quantum rules
     *
     * @param context command context
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

        final long time = this.timer.getAverageTime();

        final long seconds = time / 1000;

        final TextComponent.Builder timeComponent = Component.text()
                .append(this.config.PREFIX)
                .append(MiniMessage.get().parse(" <gray>On average, Quantum has spent <aqua>" + seconds + " seconds</aqua> searching for locations."));
        audience.sendMessage(timeComponent);
    }

}
