package sh.kaden.quantum.paper.command;

import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

public class CommandService {

    private final @NonNull JavaPlugin plugin;
    private final @NonNull PaperCommandManager<CommandSender> manager;

    /**
     * Constructs {@code CommandService}.
     *
     * @param plugin the plugin
     */
    public CommandService(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        try {
            final @NonNull Function<CommandSender, CommandSender> mapper = Function.identity();

            this.manager = new PaperCommandManager<>(
                    this.plugin,
                    AsynchronousCommandExecutionCoordinator.simpleCoordinator(),
                    mapper,
                    mapper
            );

            if (this.manager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                this.manager.registerAsynchronousCompletions();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize CommandService", e);
        }
    }

    public @NonNull PaperCommandManager<CommandSender> manager() {
        return this.manager;
    }

}
