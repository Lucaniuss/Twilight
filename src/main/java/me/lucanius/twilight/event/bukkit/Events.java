package me.lucanius.twilight.event.bukkit;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.event.AbstractEvent;
import me.lucanius.twilight.event.TwilightEvent;
import me.lucanius.twilight.event.TwilightExecutor;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@UtilityClass
public final class Events {

    private final Twilight plugin = Twilight.getInstance();
    private final EventsListener listener = new EventsListener();

    @SneakyThrows
    public <E extends Event> void subscribe(Class<E> clazz, EventsExecutor<E> handler, EventPriority priority, boolean ignoreCancelled) {
        getHandlers(clazz).register(new RegisteredListener(listener, handler, priority, plugin, ignoreCancelled));
    }

    public <E extends Event> void subscribe(Class<E> clazz, EventsExecutor<E> handler, boolean ignoreCancelled) {
        subscribe(clazz, handler, EventPriority.NORMAL, ignoreCancelled);
    }

    public <E extends Event> void subscribe(Class<E> clazz, EventsExecutor<E> handler, EventPriority priority) {
        subscribe(clazz, handler, priority, false);
    }

    public <E extends Event> void subscribe(Class<E> clazz, EventsExecutor<E> handler) {
        subscribe(clazz, handler, EventPriority.NORMAL, false);
    }

    @SuppressWarnings("unchecked")
    public <E extends TwilightEvent> void subscribe(Class<E> event, TwilightExecutor<E> handler) {
        plugin.getEvents().subscribe((Class<? extends AbstractEvent>) event, handler);
    }

    @SneakyThrows
    private <E> HandlerList getHandlers(Class<E> clazz) {
        final Method method = Arrays.stream(clazz.getDeclaredMethods()).anyMatch(m -> m.getName().equals("getHandlerList"))
                ? clazz.getDeclaredMethod("getHandlerList")
                : clazz.getSuperclass().getDeclaredMethod("getHandlerList");

        method.setAccessible(true);
        return (HandlerList) method.invoke(null);
    }
}
