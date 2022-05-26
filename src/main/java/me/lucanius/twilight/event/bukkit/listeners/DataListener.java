package me.lucanius.twilight.event.bukkit.listeners;

import com.google.common.base.Preconditions;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileService;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Scheduler;
import me.lucanius.twilight.event.bukkit.Events;
import me.lucanius.twilight.tools.functions.Condition;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
public class DataListener {

    private final Twilight plugin = Twilight.getInstance();
    private final ProfileService service = plugin.getProfiles(); // lazy init
    private final boolean caching = plugin.getConfig().getBoolean("CACHE.ENABLED");

    public DataListener() {
        Events.subscribe(AsyncPlayerPreLoginEvent.class, event -> service.getOrCreate(event.getUniqueId()).load());

        Events.subscribe(PlayerJoinEvent.class, event -> {
            final Player player = event.getPlayer();
            final UUID uniqueId = player.getUniqueId();
            final Profile profile = service.get(uniqueId);
            if (profile == null) {
                player.sendMessage(CC.RED + "Contact an administrator immediately or try to re-log!");
                return;
            }

            if (!profile.isLoaded()) {
                Scheduler.runAsync(profile::load);
            }

            plugin.getLobby().toLobby(player, profile, true);
        });

        Events.subscribe(PlayerQuitEvent.class, event -> {
            final UUID uuid = event.getPlayer().getUniqueId();
            final Profile profile = service.get(uuid);
            Preconditions.checkNotNull(profile, "Profile cannot be null!");

            Condition.of(caching, () -> service.getCache().put(uuid, profile));
            service.remove(uuid);
        });

        //TODO: create PlayerQuitEvent and save user to mongo and remove from game if they are in game
    }
}
