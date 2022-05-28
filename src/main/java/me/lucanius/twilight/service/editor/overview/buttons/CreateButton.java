package me.lucanius.twilight.service.editor.overview.buttons;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.service.editor.main.EditorMenu;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;
import me.lucanius.twilight.service.profile.modules.EditorProfile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.item.ItemBuilder;
import me.lucanius.twilight.tools.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
@RequiredArgsConstructor
public class CreateButton extends Button {

    private final int number;
    private final EditorProfile profile;

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.MINECART)
                .setName(CC.MAIN + CC.BOLD + "Create")
                .addLore(CC.SECOND + CC.ITALIC + "Click to create loadout " + (number + 1))
                .build();
    }

    @Override
    public void onClick(Player player, ClickType clickType) {
        Loadout selected = profile.getSelectedLoadout();
        if (selected == null) {
            player.closeInventory();
            return;
        }

        PersonalLoadout loadout = new PersonalLoadout(number, "Loadout " + (number + 1), selected.getContents(), "Loadout " + (number + 1));
        loadout.setContents(selected.getContents());

        profile.replace(selected.getName(), number, loadout);

        Tools.playSuccess(player);
        profile.setEditingLoadout(loadout);
        new EditorMenu(player, profile, loadout, selected).open();
    }
}
