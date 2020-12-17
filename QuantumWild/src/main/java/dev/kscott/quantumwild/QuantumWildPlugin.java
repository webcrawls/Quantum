package dev.kscott.quantumwild;

import dev.kscott.quantum.api.QuantumAPI;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class QuantumWildPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        if (this.getServer().getPluginManager().isPluginEnabled("Quantum")) {
            this.getLogger().severe("Quantum was not found! Please download it to use QuantumWild.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        final @Nullable QuantumAPI quantumAPI =  this.getServer().getServicesManager().load(QuantumAPI.class);

        if (quantumAPI == null ){
            throw new RuntimeException("Could not load the QuantumAPI! Please contact bluely with this error.");
        }


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
