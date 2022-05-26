package me.lucanius.twilight.service.queue;

import lombok.Getter;
import lombok.SneakyThrows;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.modules.DuoQueue;
import me.lucanius.twilight.service.queue.modules.RankedQueue;
import me.lucanius.twilight.service.queue.modules.UnrankedQueue;
import me.lucanius.twilight.service.queue.task.QueueTask;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class QueueService {

    private final Twilight plugin;
    private final Set<AbstractQueue<?>> queues;
    @Getter private final QueueTask task;
    private final Map<UUID, AbstractQueueData<?>> queueing;

    public QueueService(Twilight plugin) {
        this.plugin = plugin;
        this.queues = new HashSet<>();
        (this.task = new QueueTask()).runTaskTimerAsynchronously(plugin, 20L, 20L);
        this.queueing = new HashMap<>();

        registerModules();
    }

    public UnrankedQueue getUnranked() {
        return (UnrankedQueue) queues.stream().filter(queue -> queue instanceof UnrankedQueue).findFirst().orElse(null);
    }

    public RankedQueue getRanked() {
        return (RankedQueue) queues.stream().filter(queue -> queue instanceof RankedQueue).findFirst().orElse(null);
    }

    public DuoQueue getDuos() {
        return (DuoQueue) queues.stream().filter(queue -> queue instanceof DuoQueue).findFirst().orElse(null);
    }

    public Collection<AbstractQueue<?>> getAll() {
        return queues;
    }

    public AbstractQueueData<?> getData(UUID uniqueId) {
        return queueing.get(uniqueId);
    }

    public Collection<UUID> getQueued() {
        return queueing.keySet();
    }

    public int getSize() {
        return queueing.values().size();
    }

    public int getSize(Loadout loadout, AbstractQueue<?> queue) {
        return (int) queueing.values().stream().filter(data -> data.getLoadout().equals(loadout) && data.getQueue().equals(queue)).count();
    }

    public void putData(Player player, UUID uniqueId, AbstractQueueData<?> queueData) {
        queueing.put(uniqueId, queueData);
        plugin.getProfiles().get(uniqueId).setState(ProfileState.QUEUE);

        player.getInventory().clear();
        plugin.getLobby().getQueueItems().forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItem()));
    }

    public void removeData(Player player, UUID uniqueId) {
        queueing.remove(uniqueId);
        plugin.getLobby().toLobby(player, false);
    }

    @SneakyThrows
    private void registerModules() {
        for (Class<?> clazz : plugin.getRegistration().getClassesInPackage("me.lucanius.twilight.service.queue.modules")) {
            queues.add((AbstractQueue<?>) clazz.newInstance());
        }
    }
}
