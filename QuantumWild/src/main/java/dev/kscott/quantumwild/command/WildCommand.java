package dev.kscott.quantumwild.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import dev.kscott.quantumwild.config.Config;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.management.MemoryNotificationInfo;

/**
 * Handles /wild commands & subcommands
 */
public class WildCommand {

    /**
     * CommandManager reference
     */
    private final @NonNull CommandManager<CommandSender> commandManager;

    /**
     * LocationProvider reference
     */
    private final @NonNull LocationProvider locationProvider;

    /**
     * RulesetRegistry reference
     */
    private final @NonNull RulesetRegistry rulesetRegistry;

    /**
     * JavaPlugin reference
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * Config reference
     */
    private final @NonNull Config config;

    /**
     * BukkitAudiences reference
     */
    private final @NonNull BukkitAudiences audiences;

    /**
     * Constructs WildCommand
     *
     * @param audiences        {@link this#audiences}
     * @param config           {@link this#config}
     * @param rulesetRegistry  {@link this#rulesetRegistry}
     * @param locationProvider {@link this#locationProvider}
     * @param commandManager   {@link this#commandManager}
     * @param plugin           {@link this#plugin}
     */
    @Inject
    public WildCommand(
            final @NonNull BukkitAudiences audiences,
            final @NonNull Config config,
            final @NonNull RulesetRegistry rulesetRegistry,
            final @NonNull LocationProvider locationProvider,
            final @NonNull CommandManager<CommandSender> commandManager,
            final @NonNull JavaPlugin plugin
    ) {
        this.audiences = audiences;
        this.config = config;
        this.rulesetRegistry = rulesetRegistry;
        this.locationProvider = locationProvider;
        this.commandManager = commandManager;
        this.plugin = plugin;

        this.setupCommands();
    }

    /**
     * Initializes & registers commands
     */
    private void setupCommands() {
        final Command.Builder<CommandSender> builder = this.commandManager.commandBuilder("wild", "w");

        this.commandManager.command(builder.handler(this::handleWild));
    }

    /**
     * Handles /wild
     *
     * @param context command context
     */
    private void handleWild(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();

        if (!(sender instanceof Player)) {
            return;
        }

        final @NonNull Player player = (Player) sender;

        final @NonNull World world = player.getWorld();

        final @Nullable QuantumRuleset ruleset = this.config.getRuleset(world);

        if (ruleset == null) {
            this.audiences.sender(sender).sendMessage(MiniMessage.get().parse("<red>You can not use /wild in this world.</red>"));
            return;
        }

        this.locationProvider.getSpawnLocation(ruleset)
                .thenAccept(location -> {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (location.getLocation() == null) {
                                audiences.sender(sender).sendMessage(MiniMessage.get().parse("<red>Quantum was unable to locate a spawn for you.</red>"));
                                return;
                            }

                            player.teleportAsync(location.getLocation());
                        }
                    }.runTask(plugin);
                });
    }


}
