package dev.kscott.quantum.command;

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

/**
 * The base /quantum command.
 */
public class QuantumCommand {

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
    private final JavaPlugin plugin;

    /**
     * Constructs QuantumCommand
     *
     * @param commandManager CommandManager reference
     * @param locationProvider LocationProvider reference
     * @param plugin JavaPlugin reference
     */
    @Inject
    public QuantumCommand(final JavaPlugin plugin, final @NonNull CommandManager<CommandSender> commandManager, final @NonNull RulesetRegistry rulesetRegistry, final @NonNull LocationProvider locationProvider) {
        this.commandManager = commandManager;
        this.plugin = plugin;
        this.locationProvider = locationProvider;
        this.rulesetRegistry = rulesetRegistry;
        setupCommands();
    }

    /**
     * Sets up commands.
     */
    private void setupCommands() {
        final Command.Builder<CommandSender> builder = this.commandManager.commandBuilder("quantum", "q");

        this.commandManager.command(builder.literal(
                "test",
                Description.of("Test the LocationProvider")
                )
                        .handler(this::handleTest)
        );

        this.commandManager.command(builder.literal(
                "test2"
                )
                .handler(this::handleTest2)
        );
    }

    private void handleTest2(CommandContext<CommandSender> context) {
        CommandSender sender = context.getSender();

        new BukkitRunnable() {
            long lastMs = 0;
            @Override
            public void run() {
                System.out.println(plugin.getServer().getCurrentTick()+ " - "+System.currentTimeMillis()+" ("+(System.currentTimeMillis() - lastMs)+")");
                sender.sendMessage("test" + plugin.getServer().getCurrentTick());
                lastMs = System.currentTimeMillis();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    /**
     * Handles /wild
     *
     * @param context CommandContext
     */
    private void handleTest(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();

        sender.sendMessage("poop");

        if (sender instanceof Player) {
            final @NonNull Player player = (Player) sender;
            locationProvider.getSpawnLocation(rulesetRegistry.getRuleset("basic")).thenAccept(loc -> {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        System.out.println("test");
                        player.teleportAsync(loc);
                    }
                }.runTask(plugin);
            });
        }
    }

}
