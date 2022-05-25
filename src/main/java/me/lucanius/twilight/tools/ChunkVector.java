package me.lucanius.twilight.tools;

import lombok.Data;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 * I don't know who the author is.
 */
@Data
public class ChunkVector {

    private final int x, z;

    public static ChunkVector of(Chunk chunk) {
        return new ChunkVector(chunk.getX(), chunk.getZ());
    }

    public static ChunkVector ofBlock(int x, int y, int z) {
        return new ChunkVector(x >> 4, z >> 4);
    }

    public static ChunkVector ofBlock(Vector pos) {
        return ofBlock(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }

    public static ChunkVector ofBlock(Block block) {
        return ofBlock(block.getX(), block.getY(), block.getZ());
    }

    public static ChunkVector ofBlock(BlockState block) {
        return ofBlock(block.getX(), block.getY(), block.getZ());
    }

    public int getBlockMinX() {
        return x << 4;
    }

    public int getBlockMinZ() {
        return z << 4;
    }

    public BlockVector getBlockMin() {
        return new BlockVector(getBlockMinX(), 0, getBlockMinZ());
    }

    public BlockVector chunkToWorld(int x, int y, int z) {
        return new BlockVector(x + getBlockMinX(), y, z + getBlockMinZ());
    }

    public BlockVector chunkToWorld(Vector pos) {
        return chunkToWorld(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }

    public BlockVector worldToChunk(int x, int y, int z) {
        return new BlockVector(x - getBlockMinX(), y, z - getBlockMinZ());
    }

    public BlockVector worldToChunk(Vector pos) {
        return worldToChunk(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }
}
