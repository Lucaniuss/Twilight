package me.lucanius.twilight.tools.location;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.twilight.Twilight;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * I don't know who the author is.
 * @edited Lucanius
 */
@Getter @Setter
public class Cuboid {

    private final static Twilight plugin = Twilight.getInstance();

    private String world;
    private int x1;
    private int y1;
    private int z1;
    private int x2;
    private int y2;
    private int z2;

    private World bukkitWorld;

    public Cuboid(SerializableLocation first, SerializableLocation second) {
        this.world = first.getWorld();
        this.x1 = (int) Math.min(first.getX(), second.getX());
        this.y1 = (int) Math.min(first.getY(), second.getY());
        this.z1 = (int) Math.min(first.getZ(), second.getZ());
        this.x2 = (int) Math.max(first.getX(), second.getX());
        this.y2 = (int) Math.max(first.getY(), second.getY());
        this.z2 = (int) Math.max(first.getZ(), second.getZ());
    }

    public Cuboid(Location first, Location second) {
        this.world = first.getWorld().getName();
        this.x1 = Math.min(first.getBlockX(), second.getBlockX());
        this.y1 = Math.min(first.getBlockY(), second.getBlockY());
        this.z1 = Math.min(first.getBlockZ(), second.getBlockZ());
        this.x2 = Math.max(first.getBlockX(), second.getBlockX());
        this.y2 = Math.max(first.getBlockY(), second.getBlockY());
        this.z2 = Math.max(first.getBlockZ(), second.getBlockZ());
    }

    public List<Chunk> getChunks() {
        World world = getBukkitWorld();
        int x1 = this.x1 & -16;
        int x2 = this.x2 & -16;
        int z1 = this.z1 & -16;
        int z2 = this.z2 & -16;

        List<Chunk> result = new ArrayList<>(x2 - x1 + 16 + (z2 - z1) * 16);
        for (int x3 = x1; x3 <= x2; x3 += 16) {
            for (int z3 = z1; z3 <= z2; z3 += 16) {
                result.add(world.getChunkAt(x3 >> 4, z3 >> 4));
            }
        }

        return result;
    }

    public World getBukkitWorld() {
        return bukkitWorld = bukkitWorld != null ? bukkitWorld : world != null ? Optional.ofNullable(plugin.getServer().getWorld(world)).orElse(plugin.getServer().getWorlds().get(0)) : plugin.getServer().getWorlds().get(0);
    }
}
