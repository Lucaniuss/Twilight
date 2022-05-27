package me.lucanius.twilight.service.editor.select;

import me.lucanius.twilight.service.editor.select.buttons.EditorSelectButton;
import me.lucanius.twilight.tools.menu.Button;
import me.lucanius.twilight.tools.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
public class EditorSelectMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Loadout Editor";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        plugin.getLoadouts().getAll().forEach(loadout ->
                buttons.put(buttons.size(), new EditorSelectButton(loadout, plugin.getProfiles().get(player.getUniqueId()).getEditorProfile()))
        );

        fillEmptyWithGlass(buttons);

        return buttons;
    }
}
