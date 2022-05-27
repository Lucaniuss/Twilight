package me.lucanius.twilight.service.editor.overview.buttons;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.service.editor.overview.EditorOverviewMenu;
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
public class DestroyButton extends Button {

    private final PersonalLoadout personal;
    private final EditorProfile profile;

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.REDSTONE)
                .setName(CC.MAIN + CC.BOLD + "Destroy")
                .setLore(CC.SECOND + CC.ITALIC + "Click to destroy " + personal.getDisplayName())
                .build();
    }

    @Override
    public void onClick(Player player, ClickType clickType) {
        Loadout selected = profile.getSelectedLoadout();

        Tools.playSuccess(player);
        profile.destroy(selected.getName(), personal);

        new EditorOverviewMenu(selected, profile).open(player);
    }
}
