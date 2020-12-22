package dev.kscott.quantumspawn;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class QuantumSpawnPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new Metrics(this, 9727);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
