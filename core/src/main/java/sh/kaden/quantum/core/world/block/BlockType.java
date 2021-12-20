package sh.kaden.quantum.core.world.block;

import org.checkerframework.checker.nullness.qual.NonNull;

public class BlockType {

    private final @NonNull String id;

    public BlockType(final @NonNull String id) {
        this.id = id;
    }

    public @NonNull String id() {
        return this.id;
    }

}
