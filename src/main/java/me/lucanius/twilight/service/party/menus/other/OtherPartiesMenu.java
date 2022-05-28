package me.lucanius.twilight.service.party.menus.other;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.service.party.Party;
import me.lucanius.twilight.service.party.menus.other.buttons.OtherPartiesButton;
import me.lucanius.twilight.tools.menu.Button;
import me.lucanius.twilight.tools.menu.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
@RequiredArgsConstructor
public class OtherPartiesMenu extends PaginatedMenu {

    private final Party party;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Other Parties";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        plugin.getParties().getParties().stream().filter(p -> p != party).forEach(p ->
                buttons.put(buttons.size(), new OtherPartiesButton(p))
        );

        return buttons;
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 3 * 9;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
