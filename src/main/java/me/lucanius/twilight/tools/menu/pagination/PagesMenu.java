package me.lucanius.twilight.tools.menu.pagination;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.tools.menu.Button;
import me.lucanius.twilight.tools.menu.Menu;
import me.lucanius.twilight.tools.menu.PaginatedMenu;
import me.lucanius.twilight.tools.menu.pagination.buttons.BackButton;
import me.lucanius.twilight.tools.menu.pagination.buttons.JumpButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@RequiredArgsConstructor
public class PagesMenu extends Menu {

    private final PaginatedMenu menu;

    @Override
    public String getTitle(Player player) {
        return "Switch To Page";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(this.menu));

        int index = 10;
        for (int i = 1; i <= this.menu.getPages(player); i++) {
            buttons.put(index++, new JumpButton(i, this.menu, this.menu.getPage() == i));
            if ((index - 8) % 9 == 0) {
                index += 2;
            }
        }

        return buttons;
    }
}
