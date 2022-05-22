package me.lucanius.twilight.tools;

import lombok.experimental.UtilityClass;
import me.lucanius.twilight.Twilight;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@UtilityClass
public final class Scheduler {

    private final Twilight plugin = Twilight.getInstance();

    public BukkitTask run(Runnable runnable) {
        return plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    public BukkitTask runAsync(Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public BukkitTask runLater(Runnable runnable, long delay) {
        return plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public BukkitTask runLaterAsync(Runnable runnable, long delay) {
        return plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    public BukkitTask runTimer(Runnable runnable, long delay, long interval) {
        return plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, interval);
    }

    public BukkitTask runTimerAsync(Runnable runnable, long delay, long interval) {
        return plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, interval);
    }
}
