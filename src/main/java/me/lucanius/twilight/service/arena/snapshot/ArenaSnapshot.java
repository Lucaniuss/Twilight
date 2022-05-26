package me.lucanius.twilight.service.arena.snapshot;

import lombok.Getter;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.tools.ChunkVector;
import org.bukkit.ChunkSnapshot;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since May 25, 2022
 *
 * I don't know if this will even work
 */
public class ArenaSnapshot {

    @Getter private final Arena arena;
    private final Map<ChunkVector, ChunkSnapshot> snapshots;

    public ArenaSnapshot(Arena arena) {
        this.arena = arena;
        this.snapshots = new HashMap<>();
    }

    public void save() {
        arena.getCuboid().getChunks().forEach(chunk ->
                snapshots.put(ChunkVector.of(chunk), chunk.getChunkSnapshot())
        );
    }

    public void restore() {
        arena.getCuboid().getChunks().forEach(chunk -> {
            ChunkVector chunkVector = ChunkVector.of(chunk);
            ChunkSnapshot snapshot = snapshots.get(chunkVector);
            if (snapshot != null) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 256; y++) {
                            chunk.getBlock(x, y, z).getState().setData(
                                    new MaterialData(
                                            snapshot.getBlockTypeId(x, y, z),
                                            (byte) snapshot.getBlockData(x, y, z)
                                    )
                            );
                        }
                    }
                }
            }
        });
    }
}
