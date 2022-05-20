package me.lucanius.prac.profile;

import me.lucanius.prac.Twilight;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
public class ProfileService {

    private final Twilight plugin;
    private final Map<UUID, Profile> profiles;

    public ProfileService(Twilight plugin) {
        this.plugin = plugin;
        this.profiles = new ConcurrentHashMap<>();
    }

    public Profile getOrCreate(UUID uniqueId) {
        return profiles.computeIfAbsent(uniqueId, Profile::new);
    }

    public Profile get(UUID uniqueId) {
        return profiles.get(uniqueId);
    }
}
