package me.lucanius.twilight.service.lobby.hider;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.lobby.hotbar.AbstractHotbar;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.tools.Scheduler;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
public abstract class AbstractHider extends AbstractHotbar {

    protected final Twilight plugin;
    private final String permission;

    public AbstractHider(Twilight plugin) {
        this.plugin = plugin;
        this.permission = "twilight.playerview";
    }

    public void updateView() {
        Scheduler.runAsync(this::updateSync);
    }

    private void updateSync() {
        Collection<? extends Player> all = plugin.getOnline();
        Scheduler.run(() -> all.forEach(online -> {
            Profile profile = plugin.getProfiles().get(online.getUniqueId());
            if (profile.getState() != ProfileState.PLAYING) {
                all.forEach(other -> {
                    if (plugin.getProfiles().get(other.getUniqueId()).getState() != ProfileState.PLAYING) {
                        if (other.hasPermission(permission)) {
                            online.showPlayer(other);
                        } else {
                            online.hidePlayer(other);
                        }
                    }
                });
            }
        }));
    }
}
