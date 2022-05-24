package me.lucanius.twilight.service.arena.generator;

import com.boydti.fawe.object.schematic.Schematic;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Clickable;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.location.SerializableLocation;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class ArenaGenerator {

    private final static Twilight plugin = Twilight.getInstance();

    private final Arena arena;
    private final World world;
    private final Schematic schematic;

    private int multiplier = 10000;

    @SuppressWarnings("deprecation")
    public ArenaGenerator(Arena arena) {
        this.arena = arena;
        this.world = arena.getMin().getBukkitWorld();
        this.schematic = new Schematic(
                new CuboidRegion(
                        new BukkitWorld(this.world),
                        new Vector(arena.getMin().getX(), arena.getMin().getY(), arena.getMin().getZ()),
                        new Vector(arena.getMax().getX(), arena.getMax().getY(), arena.getMax().getZ())
                )
        );
    }

    public void generate() {
        if (arena.getCopies() == null) {
            arena.setCopies(new ArrayList<>());
        }

        if (arena.getMiddle() == null) {
            if (arena.getBuildHeight() == 0.0d) {
                arena.setBuildHeight((arena.getA().getY() + arena.getB().getY()) / 2);
            }

            arena.setMiddle(new SerializableLocation(world.getName(), (arena.getA().getX() + arena.getB().getX()) / 2, arena.getBuildHeight(), (arena.getA().getZ() + arena.getB().getZ()) / 2));
        }

        double x = arena.getMiddle().getX();
        double z = arena.getMiddle().getZ();

        Region region = Objects.requireNonNull(schematic.getClipboard()).getRegion();
        if (region == null) {
            Tools.log("&cFailed to generate arena: region is null");
            return;
        }

        int minX = (int) (x - region.getWidth() + multiplier);
        int maxX = (int) (x + region.getWidth() + multiplier);
        int minZ = (int) (z - region.getLength() + multiplier);
        int maxZ = (int) (z + region.getLength() + multiplier);
        int minY = (int) arena.getMin().getY();
        int maxY = (int) arena.getMax().getY();

        boolean canPaste = true;
        for (int x1 = minX; x1 <= maxX; x1++) {
            for (int z1 = minZ; z1 <= maxZ; z1++) {
                for (int y1 = minY; y1 <= maxY; y1++) {
                    if (world.getBlockAt(x1, y1, z1).getType() != Material.AIR) {
                        canPaste = false;
                        break;
                    }
                }
            }
        }

        if (!canPaste) {
            Tools.log("&cFailed to generate arena: arena is not empty, increasing multiplier and trying again");
            multiplier += 10000;
            generate();
            return;
        }

        double usedX = x + multiplier;
        double usedZ = z + multiplier;
        double usedY = arena.getMiddle().getY();

        Tools.log("&aPasting arena...");
        TaskManager.IMP.async(() -> {
            schematic.paste(region.getWorld(), new Vector(usedX, usedY, usedZ));

            try {
                Thread.sleep(1000L);
            } catch (final Exception e) {
                e.printStackTrace();
            }

            SerializableLocation min = arena.getMin();
            SerializableLocation max = arena.getMax();
            SerializableLocation a = arena.getA();
            SerializableLocation b = arena.getB();
            Arena newArena = new Arena(
                    String.valueOf((arena.getCopies().size() + 1)),
                    new SerializableLocation(world.getName(), min.getX() + multiplier, min.getY(), min.getZ() + multiplier),
                    new SerializableLocation(world.getName(), max.getX() + multiplier, max.getY(), max.getZ() + multiplier),
                    new SerializableLocation(world.getName(), a.getX() + multiplier, a.getY(), a.getZ() + multiplier),
                    new SerializableLocation(world.getName(), b.getX() + multiplier, b.getY(), b.getZ() + multiplier)
            );
            arena.getCopies().add(newArena);

            Tools.log("&aArena successfully pasted!");
            Clickable clickable = new Clickable(
                    CC.MAIN + CC.BOLD + "(Twilight) " + CC.SECOND + "Generated an arena! " + CC.GRAY + "(Click to teleport)",
                    CC.SECOND + CC.ITALIC + "Click to teleport",
                    "/tp " + newArena.getA().getX() + " " + newArena.getA().getY() + " " + newArena.getA().getZ()
            );
            plugin.getOnline().stream().filter(online -> online.hasPermission("twilight.admin")).forEach(clickable::send);
        });
    }
}
