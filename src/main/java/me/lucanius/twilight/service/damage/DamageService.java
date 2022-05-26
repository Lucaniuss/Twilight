package me.lucanius.twilight.service.damage;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public class DamageService {

    private final Map<UUID, CachedDamage> cached = new HashMap<>();

    public void cache(UUID uniqueId, CachedDamage toCache) {
        cached.put(uniqueId, toCache);
    }

    public CachedDamage retrieve(UUID uniqueId) {
        return cached.get(uniqueId);
    }

    public void destroy(UUID uniqueId) {
        cached.remove(uniqueId);
    }

    public boolean has(UUID uniqueId) {
        return cached.containsKey(uniqueId);
    }

    public Player get(UUID uniqueId) {
        CachedDamage cachedDamage = cached.get(uniqueId);

        if (cachedDamage == null) return null;
        if (System.currentTimeMillis() - cachedDamage.getTimeStamp() >= 5000L) return null;
        if (cachedDamage.getDamager().getUniqueId() == uniqueId) return null;

        return cachedDamage.getDamager();
    }
}
