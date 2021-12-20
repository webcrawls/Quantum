package sh.kaden.quantum.paper.command;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.Quantum;

public final class QuantumCommand implements PluginCommand {

    private final @NonNull Quantum quantum;

    public QuantumCommand(final @NonNull Quantum quantum) {
        this.quantum = quantum;
    }

    @Override
    public void register(@NonNull PaperCommandManager<CommandSender> manager) {
        final Command.Builder<CommandSender> builder = manager.commandBuilder("quantum", "q");

        manager.command(builder
                .literal("rules")
                .handler(this::handleRules));
    }

    private void handleRules(final @NonNull CommandContext<CommandSender> ctx) {
        final CommandSender sender = ctx.getSender();

    }

}
