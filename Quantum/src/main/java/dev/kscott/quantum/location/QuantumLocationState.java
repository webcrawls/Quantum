package dev.kscott.quantum.location;

import dev.kscott.quantum.rule.ruleset.QuantumRuleset;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;

import java.util.concurrent.CompletableFuture;

final class QuantumLocationState {

    private int x;

    private int y;

    private int z;

    private boolean isValid;

    private World world;

    private QuantumRuleset quantumRuleset;

    private CompletableFuture<Chunk> chunkFuture;

    private Chunk chunk;

    private ChunkSnapshot snapshot;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ChunkSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(ChunkSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public CompletableFuture<Chunk> getChunkFuture() {
        return chunkFuture;
    }

    public void setChunkFuture(CompletableFuture<Chunk> chunkFuture) {
        this.chunkFuture = chunkFuture;
    }

    public QuantumRuleset getQuantumRuleset() {
        return quantumRuleset;
    }

    public void setQuantumRuleset(QuantumRuleset quantumRuleset) {
        this.quantumRuleset = quantumRuleset;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getRelativeX() {
        return x & 0b1111;
    }

    public int getRelativeZ() {
        return z & 0b1111;
    }

    public int getChunkX() {
        return x >> 4;
    }

    public int getChunkZ() {
        return z >> 4;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
