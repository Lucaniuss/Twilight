package me.lucanius.twilight.layout;

import me.lucanius.twilight.layout.provider.LinesProvider;
import me.lucanius.twilight.service.party.Party;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.board.BoardAdapter;
import me.lucanius.twilight.tools.functions.Voluntary;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
public class BoardLayout extends LinesProvider implements BoardAdapter {

    @Override
    public String getTitle(Player player) {
        return CC.MAIN + CC.BOLD + "Twilight";
    }

    @Override
    public List<String> getLines(Player player) {
        UUID uuid = player.getUniqueId();
        Profile profile = plugin.getProfiles().get(uuid);
        Voluntary<Party> party = Voluntary.ofNull(plugin.getParties().getParty(uuid));

        switch (profile.getState()) {
            case LOBBY:
                return getLobby(party);
            case PLAYING:
                return getPlaying(profile, player);
            case QUEUE:
                return getQueue(player);
        }

        return null;
    }
}
