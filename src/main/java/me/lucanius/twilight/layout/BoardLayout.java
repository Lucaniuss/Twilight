package me.lucanius.twilight.layout;

import me.lucanius.twilight.layout.provider.LinesProvider;
import me.lucanius.twilight.service.profile.Profile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.board.BoardAdapter;
import org.bukkit.entity.Player;

import java.util.List;

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
        Profile profile = plugin.getProfiles().get(player.getUniqueId());

        switch (profile.getState()) {
            case LOBBY:
                return getLobby();
            case PLAYING:
                break;
            case QUEUE:
                return getQueue(player);
        }

        return null;
    }
}
