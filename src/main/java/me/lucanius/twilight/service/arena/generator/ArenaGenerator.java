package me.lucanius.twilight.service.arena.generator;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.service.arena.generator.task.ArenaGenerateTask;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Clickable;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.location.SerializableLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
public class ArenaGenerator {

    private final Twilight plugin;
    private final Arena arena;

    private int amount;

    private final int incrementX;
    private final int incrementZ;

    private int offsetX;
    private int offsetZ;

    private Map<Location, Block> blocks;

    public ArenaGenerator(Twilight plugin, Arena arena, int amount) {
        this.plugin = plugin;
        this.arena = arena;
        this.amount = amount;

        this.incrementX = 500;
        this.incrementZ = 500;

        this.offsetX = 10000;
        this.offsetZ = 10000;

        paste();
    }

    public void generate() {
        double minX = arena.getMin().getX() + offsetX;
        double minZ = arena.getMin().getZ() + offsetZ;
        double maxX = arena.getMax().getX() + offsetX;
        double maxZ = arena.getMax().getZ() + offsetZ;

        double aX = arena.getA().getX() + offsetX;
        double aZ = arena.getA().getZ() + offsetZ;
        double bX = arena.getB().getX() + offsetX;
        double bZ = arena.getB().getZ() + offsetZ;

        SerializableLocation min = new SerializableLocation(arena.getMin().getWorld(), minX, arena.getMin().getY(), minZ, arena.getMin().getYaw(), arena.getMin().getPitch());
        SerializableLocation max = new SerializableLocation(arena.getMax().getWorld(), maxX, arena.getMax().getY(), maxZ, arena.getMax().getYaw(), arena.getMax().getPitch());
        SerializableLocation a = new SerializableLocation(arena.getA().getWorld(), aX, arena.getA().getY(), aZ, arena.getA().getYaw(), arena.getA().getPitch());
        SerializableLocation b = new SerializableLocation(arena.getB().getWorld(), bX, arena.getB().getY(), bZ, arena.getB().getYaw(), arena.getB().getPitch());

        Arena newArena = new Arena(String.valueOf((arena.getCopies().size() + 1)), min, max, a, b);
        arena.getCopies().add(newArena);

        Tools.log("&aArena successfully pasted!");
        Clickable clickable = new Clickable(
                CC.MAIN + CC.BOLD + "(Twilight) " + CC.SECOND + "Generated an arena! " + CC.GRAY + "(Click to teleport)",
                CC.SECOND + CC.ITALIC + "Click to teleport",
                "/minecraft:tp " + newArena.getA().getX() + " " + newArena.getA().getY() + " " + newArena.getA().getZ()
        );
        plugin.getOnline().stream().filter(online -> online.hasPermission("twilight.admin")).forEach(clickable::send);

        amount--;
        if (amount > 0) {
            paste();
        }
    }

    public void paste() {
        if (blocks == null) {
            Map<Location, Block> temp = fromTwoCorners(arena.getMin().getBukkitLocation(), arena.getMax().getBukkitLocation());
            blocks = new HashMap<>();
            for (Location location : temp.keySet()) {
                if (temp.get(location).getType() != Material.AIR) {
                    blocks.put(location.clone().add(offsetX, 0, offsetZ), temp.get(location));
                }
            }

            temp.clear();
        } else {
            Map<Location, Block> temp = new HashMap<>();
            for (Location location : blocks.keySet()) {
                if (blocks.get(location).getType() != Material.AIR) {
                    temp.put(location.clone().add(incrementX, 0, incrementZ), blocks.get(location));
                }
            }

            blocks.clear();
            blocks.putAll(temp);
        }

        boolean canPaste = true;
        for (Location location : blocks.keySet()) {
            for (int i = 0; i < 256; i++) {
                Location cloned = location.clone();
                if (cloned.add(i, i, i).getBlock().getType() != Material.AIR || cloned.add(-i, -i, -i).getBlock().getType() != Material.AIR) {
                    canPaste = false;
                    break;
                }
            }

            Block block = location.getBlock();
            if (block.getType() != Material.AIR) {
                canPaste = false;
                break;
            }
        }

        if (!canPaste) {
            offsetX += this.incrementX;
            offsetZ += this.incrementZ;
            paste();
            return;
        }

        new ArenaGenerateTask(arena.getMin().getWorld(), blocks) {
            @Override
            public void finished() {
                generate();
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

    public Map<Location, Block> fromTwoCorners(Location min, Location max) {
        Map<Location, Block> blocks = new HashMap<>();

        int topBlockX = (Math.max(min.getBlockX(), max.getBlockX()));
        int bottomBlockX = (Math.min(min.getBlockX(), max.getBlockX()));

        int topBlockY = (Math.max(min.getBlockY(), max.getBlockY()));
        int bottomBlockY = (Math.min(min.getBlockY(), max.getBlockY()));

        int topBlockZ = (Math.max(min.getBlockZ(), max.getBlockZ()));
        int bottomBlockZ = (Math.min(min.getBlockZ(), max.getBlockZ()));

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = min.getWorld().getBlockAt(x, y, z);
                    if (block.getType() != Material.AIR) {
                        blocks.put(new Location(min.getWorld(), x, y, z), block);
                    }
                }
            }
        }

        return blocks;
    }
}
