package me.lucanius.twilight.service.queue;

import lombok.SneakyThrows;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.service.queue.abstr.AbstractQueue;
import me.lucanius.twilight.service.queue.abstr.AbstractQueueData;
import me.lucanius.twilight.service.queue.modules.DuoQueue;
import me.lucanius.twilight.service.queue.modules.SoloQueue;
import me.lucanius.twilight.service.queue.task.QueueTask;

import java.util.*;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class QueueService {

    private final Twilight plugin;
    private final Set<AbstractQueue<?>> queues;
    private final QueueTask task;
    private final Map<UUID, AbstractQueueData<?>> queueing;

    public QueueService(Twilight plugin) {
        this.plugin = plugin;
        this.queues = new HashSet<>();
        (this.task = new QueueTask()).runTaskTimerAsynchronously(plugin, 20L, 20L);
        this.queueing = new HashMap<>();

        registerModules();
    }

    public SoloQueue getSoloQueue() {
        return (SoloQueue) queues.stream().filter(queue -> queue instanceof SoloQueue).findFirst().orElse(null);
    }

    public DuoQueue getDuoQueue() {
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

    public void putData(UUID uniqueId, AbstractQueueData<?> queueData) {
        queueing.put(uniqueId, queueData);
        plugin.getProfiles().get(uniqueId).setState(ProfileState.QUEUE);
        // TODO: Reset player inventory and add a queue item
    }

    public void removeData(UUID uniqueId) {
        queueing.remove(uniqueId);
        // TODO: Send player back to lobby and reset their items (Probably going to create a LobbyService class for this)
    }

    @SneakyThrows
    private void registerModules() {
        for (Class<?> clazz : plugin.getRegistration().getClassesInPackage("me.lucanius.twilight.service.queue.modules")) {
            queues.add((AbstractQueue<?>) clazz.newInstance());
        }
    }
}
