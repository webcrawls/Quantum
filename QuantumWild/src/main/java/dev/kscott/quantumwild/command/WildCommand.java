package dev.kscott.quantumwild.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import dev.kscott.quantumwild.config.Config;
import dev.kscott.quantumwild.config.Lang;
import dev.kscott.quantumwild.wild.WildManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Handles /wild commands & subcommands.
 */
public class WildCommand {

    /**
     * CommandManager reference.
     */
    private final @NonNull CommandManager<CommandSender> commandManager;

    /**
     * Lang reference.
     */
    private final @NonNull Lang lang;

    /**
     * Config reference.
     */
    private final @NonNull Config config;

    /**
     * WildManager reference.
     */
    private final @NonNull WildManager wildManager;

    /**
     * BukkitAudiences reference.
     */
    private final @NonNull BukkitAudiences audiences;

    /**
     * Constructs WildCommand.
     *
     * @param lang           {@link this#lang}.
     * @param config         {@link this#config}.
     * @param audiences      {@link this#audiences}.
     * @param commandManager {@link this#commandManager}.
     * @param wildManager    {@link this#wildManager}.
     */
    @Inject
    public WildCommand(
            final @NonNull Lang lang,
            final @NonNull Config config,
            final @NonNull BukkitAudiences audiences,
            final @NonNull CommandManager<CommandSender> commandManager,
            final @NonNull WildManager wildManager
    ) {
        this.config = config;
        this.lang = lang;
        this.audiences = audiences;
        this.commandManager = commandManager;
        this.wildManager = wildManager;

        this.setupCommands();
    }

    /**
     * Initializes & registers commands.
     */
    private void setupCommands() {
        final Command.Builder<CommandSender> builder = this.commandManager.commandBuilder("wild", "w");

        final CommandArgument<CommandSender, String> arg1 = StringArgument.<CommandSender>newBuilder("arg1")
                .asOptional()
                .withSuggestionsProvider((ctx, arg) -> {
                    final @NonNull Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                    final @NonNull List<World> worlds = Bukkit.getWorlds();

                    final @NonNull List<String> list = new ArrayList<>(players.size() + worlds.size());

                    for (final @NonNull Player player : players) {
                        list.add(player.getName());
                    }

                    for (final @NonNull World world : worlds) {
                        list.add(world.getName());
                    }

                    return list;
                })
                .build();

        final CommandArgument<CommandSender, String> arg2 = StringArgument.<CommandSender>newBuilder("arg2")
                .asOptional()
                .withSuggestionsProvider((ctx, arg) -> {
                    final @NonNull Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                    final @NonNull List<World> worlds = Bukkit.getWorlds();

                    final @NonNull List<String> list = new ArrayList<>(players.size() + worlds.size());

                    for (final @NonNull Player player : players) {
                        list.add(player.getName());
                    }

                    for (final @NonNull World world : worlds) {
                        list.add(world.getName());
                    }

                    return list;
                })
                .build();

        this.commandManager.command(
                builder.handler(this::handleWild)
                        .argument(arg1)
                        .argument(arg2)
                        .permission("quantum.wild.use"));

    }

    /**
     * Handles /wild.
     *
     * @param context command context.
     */
    private void handleWild(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();

        final @NonNull Optional<String> arg1Optional = context.getOptional("arg1");
        final @NonNull Optional<String> arg2Optional = context.getOptional("arg2");


        @Nullable Player player = null;
        @Nullable World world = null;

        if (arg1Optional.isEmpty() && arg2Optional.isEmpty()) {
            if (sender instanceof Player) {
                player = (Player) sender;
                world = player.getWorld();
            } else {
                audiences.sender(sender).sendMessage(lang.c("no-console"));
                return;
            }
        }

        ArgLocation worldArgLocation = ArgLocation.UNK;

        if (arg1Optional.isPresent()) {
            final @NonNull String arg1 = arg1Optional.get();

            player = Bukkit.getPlayer(arg1);

            if (player == null) {
                world = Bukkit.getWorld(arg1);
                worldArgLocation = ArgLocation.FIRST;

                if (world != null) {
                    if (!sender.hasPermission("quantum.wild.world." + arg1)) {
                        audiences.sender(sender).sendMessage(lang.c("no-permission"));
                        return;
                    }
                }
            } else {
                if (!sender.hasPermission("quantum.wild.others")) {
                    audiences.sender(sender).sendMessage(lang.c("no-permission"));
                    return;
                }
            }
        }

        if (arg2Optional.isPresent()) {
            final @NonNull String arg2 = arg2Optional.get();

            player = Bukkit.getPlayer(arg2);

            if (player == null) {
                world = Bukkit.getWorld(arg2);
                worldArgLocation = ArgLocation.SECOND;

                if (world != null) {
                    if (!sender.hasPermission("quantum.wild.world." + arg2)) {
                        audiences.sender(sender).sendMessage(lang.c("no-permission"));
                        return;
                    }
                }
            } else {
                if (!sender.hasPermission("quantum.wild.others")) {
                    audiences.sender(sender).sendMessage(lang.c("no-permission"));
                    return;
                }
            }

        }

        if (player != null && (world == null && worldArgLocation == ArgLocation.UNK)) {
            world = player.getWorld();
        }

        if (player == null && world != null) {
            if (sender instanceof Player) {
                player = (Player) sender;
            }
        }

        if (player == null || world == null) {
            audiences.sender(sender).sendMessage(lang.c("wild-invalid-syntax"));
            return;
        }

        this.wildManager.wildTeleportPlayer(player, world);
    }

    private enum ArgLocation {
        UNK,
        FIRST,
        SECOND
    }
}
