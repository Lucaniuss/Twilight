package me.lucanius.twilight.layout.provider;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.tools.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public abstract class LinesProvider {

    protected final Twilight plugin = Twilight.getInstance();

    public List<String> getLobby() {
        List<String> lines = new ArrayList<>();

        lines.add(CC.SMALL_BAR);
        lines.add("&fOnline: " + CC.SECOND + plugin.getOnline().size());
        lines.add("In Game: " + CC.SECOND + 0);
        lines.add("In Queue: " + CC.SECOND + plugin.getQueues().getSize());
        lines.add(" ");
        lines.add("&7&olucanius.me");
        lines.add(CC.SMALL_BAR);

        return lines;
    }

    public List<String> getQueue(Player player) {
        List<String> lines = new ArrayList<>();
        AbstractQueueData<?> data = plugin.getQueues().getData(player.getUniqueId());

        lines.add(CC.SMALL_BAR);
        lines.add("&fOnline: " + CC.SECOND + plugin.getOnline().size());
        lines.add("In Game: " + CC.SECOND + 0);
        lines.add("In Queue: " + CC.SECOND + plugin.getQueues().getSize());
        lines.add(CC.SMALL_BAR);
        lines.add(CC.MAIN + CC.ITALIC + "Queueing " + data.getQueue().getName());
        lines.add(CC.ICON + CC.WHITE + "Time: " + CC.SECOND + data.getTime());
        lines.add(CC.ICON + CC.WHITE + "Loadout: " + CC.SECOND + data.getLoadout().getName());
        lines.add(" ");
        lines.add("&7&olucanius.me");
        lines.add(CC.SMALL_BAR);

        return lines;
    }
}
