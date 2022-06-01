package me.lucanius.twilight.layout.nametag;

import me.lucanius.label.adapter.NametagAdapter;
import me.lucanius.label.data.NametagData;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.service.profile.ProfileState;
import me.lucanius.twilight.tools.CC;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @author Lucanius
 * @since June 01, 2022.
 * Twilight - All Rights Reserved.
 */
public class NametagLayout implements NametagAdapter {

    private final Twilight plugin = Twilight.getInstance();

    @Override
    public List<NametagData> getData(Player player) {
        Profile profile = plugin.getProfiles().get(player.getUniqueId());
        List<NametagData> lobby = Collections.singletonList(new NametagData("LOBBY", 10, CC.GOLD, ""));

        if (profile == null) {
            return lobby;
        }

        if (profile.getState() != ProfileState.PLAYING) {
            return lobby;
        }

        Game game = plugin.getGames().get(profile);
        if (game == null) {
            return lobby;
        }

        GameTeam team = profile.getGameProfile().getTeam();
        if (team == null) {
            return lobby;
        }

        return Collections.singletonList(new NametagData(team.getColor().name(), 15, team.getColor().toString(), ""));
    }
}
