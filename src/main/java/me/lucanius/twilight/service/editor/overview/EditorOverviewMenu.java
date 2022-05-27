package me.lucanius.twilight.service.editor.overview;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.service.editor.overview.buttons.CreateButton;
import me.lucanius.twilight.service.editor.overview.buttons.DestroyButton;
import me.lucanius.twilight.service.editor.overview.buttons.EditButton;
import me.lucanius.twilight.service.editor.overview.buttons.RenameButton;
import me.lucanius.twilight.service.editor.select.EditorSelectMenu;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;
import me.lucanius.twilight.service.profile.modules.EditorProfile;
import me.lucanius.twilight.tools.menu.Button;
import me.lucanius.twilight.tools.menu.Menu;
import me.lucanius.twilight.tools.menu.pagination.buttons.BackButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
@RequiredArgsConstructor
public class EditorOverviewMenu extends Menu {

    private final Loadout loadout;
    private final EditorProfile profile;

    @Override
    public String getTitle(Player player) {
        return "Viewing " + loadout.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        PersonalLoadout[] loadouts = profile.getAll(loadout.getName());
        if (loadouts == null) {
            return buttons;
        }

        int pos = -1;
        for (int i = 0; i < 4; i++) {
            pos += 2;
            PersonalLoadout personal = loadouts[i];

            buttons.put(pos + 9, personal != null ? new EditButton(i, profile) : new CreateButton(i, profile));
            buttons.put(pos + 18, personal != null ? new RenameButton(loadout, personal, profile) : glass());
            buttons.put(pos + 27, personal != null ? new DestroyButton(personal, profile) : glass());
        }

        buttons.put(36, new BackButton(new EditorSelectMenu()));

        fillEmptyWithGlass(buttons);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
