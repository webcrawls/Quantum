package sh.kaden.quantum.paper.command;

import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface PluginCommand {

    void register(final @NonNull PaperCommandManager<CommandSender> manager);

}
