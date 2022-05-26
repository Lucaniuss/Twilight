package me.lucanius.twilight.service.cooldown;

import lombok.Getter;
import me.lucanius.twilight.tools.Scheduler;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
@Getter
public class Cooldown {

    private final long delay;
    private final Runnable toRun;
    private long time;

    private BukkitTask task;

    public Cooldown(long delay, Runnable toRun) {
        this.delay = delay;
        this.toRun = toRun;
        this.time = 0L;
    }

    public long calc() {
        return time + delay;
    }

    public boolean active() {
        return calc() > System.currentTimeMillis() && task != null;
    }

    public Cooldown reset() {
        time = System.currentTimeMillis();
        if (task != null) {
            task.cancel();
            task = null;
        }

        task = Scheduler.runLaterAsync(() -> {
            toRun.run();
            if (task != null) {
                task.cancel();
            }
            task = null;
        }, delay / 50L);
        return this;
    }

    public void cancel() {
        time = 0L;
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public int remaining() {
        return (int) ((calc() - System.currentTimeMillis()) / 1000);
    }
}
