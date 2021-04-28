package dev.kscott.quantum.rule.rules.sync;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.kscott.quantum.rule.option.RegionListOption;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvoidRegionRule extends SyncQuantumRule {

    final @NonNull WorldGuard worldGuard;

    boolean avoidAllRegions = false;

    @MonotonicNonNull List<String> regionIds;

    public AvoidRegionRule() {
        super(new RegionListOption());

        this.regionIds = new ArrayList<>();
        this.worldGuard = WorldGuard.getInstance();
    }

    @Override
    public void postCreation() {
        this.regionIds = Arrays.asList(this.getOption(RegionListOption.class).getValue());

        if (regionIds.size() == 0) {
            return;
        }

        if (regionIds.get(0).equals("*")) {
            avoidAllRegions = true;
            return;
        }
    }

    @Override
    public boolean validate(@NonNull Chunk chunk, int x, int y, int z) {
        // Can't figure out math rn :|
        final @NonNull Block block = chunk.getBlock(x, y, z);
        final @NonNull Location location = block.getLocation();

        final @NonNull World world = BukkitAdapter.adapt(location.getWorld());

        final @Nullable RegionManager regionManager = worldGuard.getPlatform().getRegionContainer().get(world);

        if (regionManager == null) {
            return true;
        }

        final @NonNull ApplicableRegionSet regions = regionManager.getApplicableRegions(BukkitAdapter.adapt(location).toVector().toBlockPoint());

        if (regions.size() == 0) {
            return true;
        }

        if (avoidAllRegions) {
            return false;
        }

        for (final @NonNull ProtectedRegion region : regions.getRegions()) {
            if (regionIds.contains(region.getId())) {
                return false;
            }
        }

        return true;
    }
}
