package me.lucanius.prac.service.queue;

import lombok.SneakyThrows;
import me.lucanius.prac.Twilight;
import me.lucanius.prac.service.profile.ProfileState;
import me.lucanius.prac.service.queue.abstr.AbstractQueue;
import me.lucanius.prac.service.queue.abstr.AbstractQueueData;
import me.lucanius.prac.service.queue.modules.DuoQueue;
import me.lucanius.prac.service.queue.modules.SoloQueue;
import me.lucanius.prac.service.queue.task.QueueTask;

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
        for (Class<?> clazz : plugin.getRegistration().getClassesInPackage("me.lucanius.prac.service.queue.modules")) {
            queues.add((AbstractQueue<?>) clazz.newInstance());
        }
    }
}
