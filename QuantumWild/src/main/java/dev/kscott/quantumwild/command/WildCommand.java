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
import java.util.concurrent.TimeUnit;

/**
 * Handles /wild commands & subcommands
 */
public class WildCommand {

    /**
     * CommandManager reference
     */
    private final @NonNull CommandManager<CommandSender> commandManager;

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
     * @param commandManager   {@link this#commandManager}
     * @param wildManager      {@link this#wildManager}
     */
    @Inject
    public WildCommand(
            final @NonNull Lang lang,
            final @NonNull BukkitAudiences audiences,
            final @NonNull CommandManager<CommandSender> commandManager,
            final @NonNull WildManager wildManager
    ) {
        this.lang = lang;
        this.audiences = audiences;
        this.commandManager = commandManager;
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

        if (sender instanceof Player) {
            this.wildManager.wildTeleportPlayer((Player) sender);
        } else {
            audiences.sender(sender).sendMessage(lang.c("no_console"));
        }

    }


}
