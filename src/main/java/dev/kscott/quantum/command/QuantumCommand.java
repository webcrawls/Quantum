package dev.kscott.quantum.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.Description;
import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import dev.kscott.quantum.location.LocationProvider;
import org.bukkit.command.CommandSender;
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
     * Constructs QuantumCommand
     * @param commandManager CommandManager reference
     */
    @Inject
    public QuantumCommand(final @NonNull CommandManager<CommandSender> commandManager, final @NonNull LocationProvider locationProvider) {
        this.commandManager = commandManager;
        this.locationProvider = locationProvider;
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
    }

    /**
     * Handles /quantum
     * @param context CommandContext
     */
    private void handleTest(final @NonNull CommandContext<CommandSender> context) {
        final @NonNull CommandSender sender = context.getSender();
    }

}
