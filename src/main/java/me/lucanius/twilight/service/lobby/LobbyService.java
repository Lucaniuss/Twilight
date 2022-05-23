package me.lucanius.twilight.service.lobby;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.lobby.hotbar.AbstractHotbar;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.location.SerializableLocation;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 23, 2022
 */
public class LobbyService extends AbstractHotbar {

    private final Twilight plugin;

    private SerializableLocation lobbyLocation;

    public LobbyService(Twilight plugin) {
        this.plugin = plugin;

        lobbyLocation = plugin.getConfig().contains("LOBBY.LOCATION")
                ? new SerializableLocation(plugin.getConfig().getString("LOBBY.LOCATION"))
                : new SerializableLocation(0.5, 60, 0.5);
    }

    public void toLobby(Player player, Profile profile) {
        profile.setState(ProfileState.LOBBY);

        Tools.clearPlayer(player);
        items.forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItem()));

        player.teleport(lobbyLocation.getBukkitLocation());
    }

    public void setLobbyLocation(SerializableLocation location) {
        lobbyLocation = location;
        plugin.getConfig().set("LOBBY.LOCATION", location.serialize());
        plugin.getConfig().save();
    }
}
