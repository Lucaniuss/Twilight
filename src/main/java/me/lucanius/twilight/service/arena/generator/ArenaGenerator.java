package me.lucanius.twilight.service.arena.generator;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.LegacyWorldData;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.arena.Arena;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Clickable;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.location.SerializableLocation;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;

/**
 * @author Lucanius
 * @since May 24, 2022
 */
public class ArenaGenerator {

    private final static Twilight plugin = Twilight.getInstance();

    private final Arena arena;
    private final World world;

    private final CuboidRegion region;
    private final BlockArrayClipboard clipboard;

    private int multiplier = 10000;

    public ArenaGenerator(Arena arena) {
        this.arena = arena;
        this.world = arena.getMin().getBukkitWorld();

        this.region = new CuboidRegion(
                FaweAPI.getWorld(arena.getMin().getWorld()),
                new Vector(arena.getMin().getX(), arena.getMin().getY(), arena.getMin().getZ()),
                new Vector(arena.getMax().getX(), arena.getMax().getY(), arena.getMax().getZ())
        );
        this.clipboard = new BlockArrayClipboard(region);
        try {
            Operations.complete(new ForwardExtentCopy(
                    FaweAPI.getWorld(arena.getMin().getWorld()), region, clipboard, region.getMinimumPoint()
            ));
        } catch (final Exception e) {
            e.printStackTrace();
        }
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

        double x = (arena.getMiddle().getX() + multiplier);
        double y = (arena.getA().getY() + arena.getB().getY()) / 2;
        double z = (arena.getMiddle().getZ() + multiplier);

        int minX = (int) (x - region.getWidth() - 200);
        int maxX = (int) (x + region.getWidth() + 200);
        int minZ = (int) (z - region.getLength() - 200);
        int maxZ = (int) (z + region.getLength() + 200);
        int minY = (int) (y - region.getHeight());
        int maxY = (int) (y + region.getHeight());

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

        Tools.log("&aPasting arena...");
        TaskManager.IMP.async(() -> {
            Tools.log("Pasting at: " + x + " " + y + " " + z);
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(FaweAPI.getWorld(arena.getMin().getWorld()), -1);
            try {
                Operation operation = new ClipboardHolder(clipboard, LegacyWorldData.getInstance())
                        .createPaste(editSession, LegacyWorldData.getInstance())
                        .to(new Vector(x, y, z))
                        .build();
                Operations.complete(operation);
            } catch (final Exception e) {
                e.printStackTrace();
            }

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
                    "/minecraft:tp " + newArena.getA().getX() + " " + newArena.getA().getY() + " " + newArena.getA().getZ()
            );
            plugin.getOnline().stream().filter(online -> online.hasPermission("twilight.admin")).forEach(clickable::send);
        });
    }
}
