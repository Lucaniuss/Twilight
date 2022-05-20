package me.lucanius.prac.service.profile;

import com.mongodb.client.model.Filters;
import lombok.Getter;
import me.lucanius.prac.Twilight;
import me.lucanius.prac.tools.Scheduler;

import java.util.Collection;
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

    @Getter private Profile dummy;

    public ProfileService(Twilight plugin) {
        this.plugin = plugin;
        this.profiles = new ConcurrentHashMap<>();

        Scheduler.run(() -> this.dummy = new Profile(UUID.fromString("00000000-0000-0000-0000-000000000000")));
    }

    public Profile getOrCreate(UUID uniqueId) {
        return profiles.computeIfAbsent(uniqueId, Profile::new);
    }

    public Profile get(UUID uniqueId) {
        return profiles.get(uniqueId);
    }

    public Profile getOffline(UUID uniqueId) {
        return profiles.computeIfAbsent(uniqueId, uuid -> {
            Profile profile = new Profile(uuid);
            profile.load(plugin.getMongo().getProfiles().find(Filters.eq("uniqueId", uuid.toString())).first());

            return profile.isLoaded() ? profile : null;
        });
    }

    public Collection<Profile> getAll() {
        return profiles.values();
    }

    public void remove(UUID uniqueId) {
        Scheduler.runAsync(() -> {
            Profile profile = profiles.remove(uniqueId);
            if (profile != null && profile.isLoaded()) {
                profile.save();
            }
        });
    }
}
