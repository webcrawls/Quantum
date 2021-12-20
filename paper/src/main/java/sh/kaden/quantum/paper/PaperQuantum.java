package sh.kaden.quantum.paper;

import cloud.commandframework.tasks.TaskFactory;
import cloud.commandframework.tasks.TaskSynchronizer;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.Quantum;
import sh.kaden.quantum.core.location.LocationProvider;
import sh.kaden.quantum.paper.location.PaperLocationProvider;
import sh.kaden.quantum.core.rule.RuleRegistry;
import sh.kaden.quantum.paper.tasks.paper.PaperTaskSynchronizer;

/**
 * Quantum's Paper implementation.
 */
public final class PaperQuantum implements Quantum {

    private final @NonNull JavaPlugin plugin;
    private final @NonNull RuleRegistry ruleRegistry;
    private final @NonNull LocationProvider locationProvider;
    private final @NonNull TaskSynchronizer taskSynchronizer;
    private final @NonNull TaskFactory taskFactory;

    public PaperQuantum(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.taskSynchronizer = new PaperTaskSynchronizer(this.plugin);
        this.taskFactory = new TaskFactory(this.taskSynchronizer);
        this.ruleRegistry = new RuleRegistry();
        this.locationProvider = new PaperLocationProvider(this.plugin, this.taskFactory);
    }



    @Override
    public @NonNull RuleRegistry ruleRegistry() {
        return this.ruleRegistry;
    }

    @Override
    public @NonNull LocationProvider locationProvider() {
        return this.locationProvider;
    }
}
