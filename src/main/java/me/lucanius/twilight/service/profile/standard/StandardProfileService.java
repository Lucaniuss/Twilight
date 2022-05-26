package me.lucanius.twilight.service.profile.standard;

import com.mongodb.client.model.Filters;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileCache;
import me.lucanius.twilight.service.profile.ProfileService;
import me.lucanius.twilight.tools.Scheduler;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
public class StandardProfileService implements ProfileService {

    private final Twilight plugin;
    private final Map<UUID, Profile> profiles;
    private final ProfileCache cache;
    private Profile dummy;

    public StandardProfileService(Twilight plugin) {
        this.plugin = plugin;
        this.profiles = new ConcurrentHashMap<>();

        (this.cache = new ProfileCache()).build(plugin.getConfig().getLong("CACHE.TIME"), TimeUnit.valueOf(plugin.getConfig().getString("CACHE.UNIT")));

        Scheduler.run(() -> this.dummy = new Profile(UUID.fromString("00000000-0000-0000-0000-000000000000")));
    }

    @Override
    public Profile getOrCreate(UUID uniqueId) {
        return profiles.computeIfAbsent(uniqueId, Profile::new);
        /*
        return Optional.ofNullable(cache.isCached(uniqueId)
                ? cache.getIfPresent(uniqueId)
                : profiles.get(uniqueId)
        ).orElseGet(() -> profiles.computeIfAbsent(uniqueId, Profile::new));
        */
    }

    @Override
    public Profile get(UUID uniqueId) {
        return profiles.get(uniqueId);
    }

    @Override
    public Profile getOffline(UUID uniqueId) {
        return profiles.computeIfAbsent(uniqueId, uuid -> {
            Profile profile = new Profile(uuid); // find document here so it runs on main thread
            profile.load(plugin.getMongo().getProfiles().find(Filters.eq("uniqueId", uuid.toString())).first());

            return profile.isLoaded() ? profile : null;
        });
        /*
        return Optional.ofNullable(cache.isCached(uniqueId)
                ? cache.getIfPresent(uniqueId)
                : profiles.get(uniqueId)
        ).orElseGet(() -> profiles.computeIfAbsent(uniqueId, uuid -> {
            Profile profile = new Profile(uuid);
            profile.load(plugin.getMongo().getProfiles().find(Filters.eq("uniqueId", uuid.toString())).first());

            return profile.isLoaded() ? profile : null;
        }));
        */
    }

    @Override
    public CompletableFuture<Profile> getAsync(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> this.getOffline(uniqueId));
    }

    @Override
    public Profile getDummy() {
        return dummy;
    }

    @Override
    public ProfileCache getCache() {
        return cache;
    }

    @Override
    public Collection<Profile> getAll() {
        return profiles.values();
    }

    @Override
    public void remove(UUID uniqueId) {
        Scheduler.runAsync(() -> {
            Profile profile = profiles.remove(uniqueId);
            if (profile != null && profile.isLoaded()) {
                profile.save();
            }
        });
    }
}
