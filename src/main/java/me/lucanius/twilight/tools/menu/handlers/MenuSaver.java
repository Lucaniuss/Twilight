package me.lucanius.twilight.tools.menu.handlers;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.lucanius.twilight.tools.menu.Menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@UtilityClass
public final class MenuSaver {

    @Getter private final Map<UUID, Menu> cached = new HashMap<>();

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
