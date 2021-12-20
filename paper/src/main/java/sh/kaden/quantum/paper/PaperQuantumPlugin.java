package sh.kaden.quantum.paper;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import sh.kaden.quantum.core.Quantum;

/**
 * Quantum's Paper entrypoint.
 */
public final class PaperQuantumPlugin extends JavaPlugin {

    private @MonotonicNonNull PaperQuantum quantum;

    @Override
    public void onEnable() {
        this.quantum = new PaperQuantum(this);

        this.getServer().getServicesManager().register(
                Quantum.class,
                this.quantum,
                this,
                ServicePriority.High
        );
    }

}
