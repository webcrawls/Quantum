package dev.kscott.quantum.rule.rules.sync;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import dev.kscott.quantum.rule.option.FactionsWhitelistOption;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvoidClaimsRule extends SyncQuantumRule {

    private final @NonNull List<String> factionsWhitelist = new ArrayList<>();

    @Override
    public void postCreation() {
        factionsWhitelist.addAll(Arrays.asList(this.getOption(FactionsWhitelistOption.class).getValue()));
    }

    @Override
    public boolean validate(@NonNull Chunk chunk, int x, int y, int z) {
        final @NonNull Location location = chunk.getBlock(x, y, z).getLocation();
        final @NonNull FLocation fLocation = new FLocation(location);

        final @Nullable Faction faction = Board.getInstance().getFactionAt(fLocation);

        return faction == null || factionsWhitelist.contains(faction.getId());
    }
}
