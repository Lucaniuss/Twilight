package me.lucanius.twilight.service.arena.generator.task;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zonix
 * @edited Lucanius
 */
public abstract class ArenaGenerateTask extends BukkitRunnable {

    private final String world;
    private final Map<Location, Block> blocks;

    public ArenaGenerateTask(String world, Map<Location, Block> blocks) {
        this.world = world;
        this.blocks = new HashMap<>(blocks);
    }

    @Override @SuppressWarnings("deprecation")
    public void run() {
        if (blocks.isEmpty()) {
            finished();
            cancel();
            return;
        }

        TaskManager.IMP.async(() -> {
            EditSession session = new EditSessionBuilder(FaweAPI.getWorld(world))
                    .fastmode(true)
                    .allowedRegionsEverywhere()
                    .autoQueue(false)
                    .limitUnlimited()
                    .build();

            for (Map.Entry<Location, Block> entry : blocks.entrySet()) {
                Location location = entry.getKey();
                Block block = entry.getValue();
                Vector vector = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                BaseBlock baseBlock = new BaseBlock(block.getTypeId(), block.getData());

                try {
                    session.setBlock(vector, baseBlock);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            session.flushQueue();
            TaskManager.IMP.task(blocks::clear);
        });
    }

    public abstract void finished();

}
