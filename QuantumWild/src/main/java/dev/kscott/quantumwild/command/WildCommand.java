package dev.kscott.quantumwild.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import dev.kscott.quantum.location.LocationProvider;
import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import dev.kscott.quantumwild.config.Config;
import dev.kscott.quantumwild.config.Lang;
import dev.kscott.quantumwild.wild.WildManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

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
     * JavaPlugin reference
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * Config reference
     */
    private final @NonNull Config config;

    /**
     * Lang reference
     */
    private final @NonNull Lang lang;

    /**
     * WildManager reference
     */
    private final @NonNull WildManager wildManager;

    /**
     * BukkitAudiences reference
     */
    private final @NonNull BukkitAudiences audiences;

    /**
     * Constructs WildCommand
     *
     * @param lang             {@link this#lang}
     * @param audiences        {@link this#audiences}
     * @param config           {@link this#config}
     * @param locationProvider {@link this#locationProvider}
     * @param commandManager   {@link this#commandManager}
     * @param plugin           {@link this#plugin}
     * @param wildManager {@link this#wildManager}
     */
    @Inject
    public WildCommand(
            final @NonNull Lang lang,
            final @NonNull BukkitAudiences audiences,
            final @NonNull Config config,
            final @NonNull LocationProvider locationProvider,
            final @NonNull CommandManager<CommandSender> commandManager,
            final @NonNull JavaPlugin plugin,
            final @NonNull WildManager wildManager
            ) {
        this.lang = lang;
        this.audiences = audiences;
        this.config = config;
        this.locationProvider = locationProvider;
        this.commandManager = commandManager;
        this.plugin = plugin;
        this.wildManager = wildManager;

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
            this.audiences.sender(sender).sendMessage(lang.c("wild.invalid_world"));
            return;
        }

        boolean canUseWild = System.currentTimeMillis() >= this.wildManager.getCurrentCooldown(player);

        if (!canUseWild) {
            this.audiences.sender(sender).sendMessage(lang.c("wild.invalid_world"));
            return;
        }

        this.locationProvider.getSpawnLocation(ruleset)
                .thenAccept(quantumLocation -> new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (quantumLocation.getLocation() == null) {
                            audiences.sender(sender).sendMessage(lang.c("wild.failed_spawn_location"));
                            return;
                        }

                        final @NonNull Location location = quantumLocation.getLocation();

                        player.teleportAsync(location.toCenterLocation()).thenAccept(success -> {
                            if (success) {
                                wildManager.applyWildCooldown(player);
                                audiences.sender(sender).sendMessage(
                                        lang.c(
                                                "wild.tp_success",
                                                Map.of(
                                                        "{x}", Double.toString(location.getBlockX()),
                                                        "{y}", Double.toString(location.getBlockY()),
                                                        "{z}", Double.toString(location.getBlockZ())
                                                )
                                        )
                                );
                            }
                        });

                    }
                }.runTask(plugin));
    }


}
