package dev.kscott.quantum.inject;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.kscott.quantum.QuantumPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * Constructs & provides the cloud CommandManager
 */
public class CommandModule extends AbstractModule {

    /**
     * Plugin reference
     */
    private final @NonNull Plugin plugin;

    /**
     * Constructs the CommandModule
     * @param plugin Plugin reference
     */
    public CommandModule(final @NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Provides the PaperCommandManager, with async completions if possible.
     * @return CommandManager
     */
    @Singleton
    public final CommandManager<CommandSender> provideCommandManager() {
        try {
            final @NonNull Function<CommandSender, CommandSender> mapper = Function.identity();

            final @NonNull PaperCommandManager<CommandSender> commandManager = new PaperCommandManager<>(
                    plugin,
                    AsynchronousCommandExecutionCoordinator.simpleCoordinator(),
                    mapper,
                    mapper
            );

            if (commandManager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                commandManager.registerAsynchronousCompletions();
            }

            return commandManager;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize the CommandManager", e);
        }
    }

}
