package me.lucanius.prac.tools.menu.handlers;

import lombok.experimental.UtilityClass;
import me.lucanius.prac.tools.menu.Menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@UtilityClass
public final class MenuSaver {

    private final Map<UUID, Menu> cached = new HashMap<>();

    public void add(UUID uniqueId, Menu menu) {
        cached.put(uniqueId, menu);
    }

    public void remove(UUID uniqueId) {
        cached.remove(uniqueId);
    }

    public Menu get(UUID uniqueId) {
        return cached.get(uniqueId);
    }
}
