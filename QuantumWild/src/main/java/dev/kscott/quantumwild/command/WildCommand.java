package dev.kscott.quantumwild.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.Description;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.rule.ruleset.RulesetRegistry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

public class WildCommand {

    private final @NonNull CommandManager<CommandSender> commandManager;

    private final @NonNull LocationProvider locationProvider;

    private final @NonNull RulesetRegistry rulesetRegistry;

    private final @NonNull JavaPlugin plugin;

    @Inject
    public WildCommand(final @NonNull RulesetRegistry rulesetRegistry, final @NonNull LocationProvider locationProvider, final @NonNull CommandManager<CommandSender> commandManager, final @NonNull JavaPlugin plugin) {
        this.commandManager = commandManager;
        this.locationProvider = locationProvider;
        this.rulesetRegistry = rulesetRegistry;
        this.plugin = plugin;
    }

    private void setupCommands() {
        final Command.Builder<CommandSender> builder = this.commandManager.commandBuilder("quantum", "q");

        this.commandManager.command(builder.literal(
                "rulesets",
                Description.of("Get the ids all loaded rulesets")
                )
                        .handler(this::handleWild)
        );
    }

    private void handleWild(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();

        if (!(sender instanceof Player)) {
            return;
        }

        final @NonNull Player player = (Player) sender;

        this.locationProvider.getSpawnLocation(rulesetRegistry.getRuleset("basic"))
                .thenAccept(location -> {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.teleportAsync(location);
                        }
                    }.runTask(plugin);
                });
    }


}
