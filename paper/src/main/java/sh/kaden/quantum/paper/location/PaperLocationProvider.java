package sh.kaden.quantum.paper.location;

import cloud.commandframework.tasks.TaskFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import sh.kaden.quantum.core.exception.QuantumWorldNotFoundException;
import sh.kaden.quantum.core.location.BlockVector;
import sh.kaden.quantum.core.location.LocationProvider;
import sh.kaden.quantum.core.SearchSettings;
import sh.kaden.quantum.paper.util.PaperAdapter;
import sh.kaden.quantum.core.world.World;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Paper's {@link LocationProvider} implementation.
 */
public final class PaperLocationProvider implements LocationProvider {

    private final @NonNull JavaPlugin plugin;
    private final @NonNull TaskFactory taskFactory;

    /**
     * Constructs {@code PaperLocationProvider}.
     *
     * @param taskFactory the task factory
     */
    public PaperLocationProvider(final @NonNull JavaPlugin plugin,
                                 final @NonNull TaskFactory taskFactory) {
        this.plugin = plugin;
        this.taskFactory = taskFactory;
    }

    @Override
    public @NonNull CompletableFuture<BlockVector> findLocation(final @NonNull SearchSettings settings) {
        final CompletableFuture<BlockVector> completableFuture = new CompletableFuture<>();

        this.taskFactory.recipe()
                .begin(settings)
                .asynchronous(_settings -> {
                    final World world = _settings.world();
                    final Optional<org.bukkit.World> bukkitWorldOpt = PaperAdapter.toBukkitWorld(world);

                    if (bukkitWorldOpt.isEmpty()) {
                        completableFuture.completeExceptionally(new QuantumWorldNotFoundException());
                    }

                    return;
                })
                .execute();

        return completableFuture;
    }
}
