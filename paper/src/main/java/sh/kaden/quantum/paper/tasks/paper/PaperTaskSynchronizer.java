package sh.kaden.quantum.paper.tasks.paper;

import cloud.commandframework.tasks.TaskConsumer;
import cloud.commandframework.tasks.TaskFunction;
import cloud.commandframework.tasks.TaskSynchronizer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PaperTaskSynchronizer implements TaskSynchronizer {

    private final @NonNull JavaPlugin plugin;
    private final @NonNull Executor executor;

    public PaperTaskSynchronizer(final @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public <I> CompletableFuture<Void> runSynchronous(@NonNull I input, @NonNull TaskConsumer<I> consumer) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                consumer.accept(input);
                future.complete(null);
            }
        }.runTask(this.plugin);

        return future;
    }

    @Override
    public <I, O> CompletableFuture<O> runSynchronous(@NonNull I input, @NonNull TaskFunction<I, O> function) {
        final CompletableFuture<O> future = new CompletableFuture<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                final O output = function.apply(input);
                future.complete(output);
            }
        }.runTask(this.plugin);

        return future;
    }

    @Override
    public <I> CompletableFuture<Void> runAsynchronous(@NonNull I input, @NonNull TaskConsumer<I> consumer) {
        return CompletableFuture.runAsync(() -> consumer.accept(input), this.executor);
    }

    @Override
    public <I, O> CompletableFuture<O> runAsynchronous(@NonNull I input, @NonNull TaskFunction<I, O> function) {
        return CompletableFuture.supplyAsync(() -> function.apply(input), this.executor);
    }
}
