package me.lucanius.twilight.service.editor.overview.buttons;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.personal.PersonalLoadout;
import me.lucanius.twilight.service.profile.modules.EditorProfile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Scheduler;
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
public class RenameButton extends Button {

    private final Loadout loadout;
    private final PersonalLoadout personal;
    private final EditorProfile profile;

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.NAME_TAG)
                .setName(CC.MAIN + CC.BOLD + "Rename")
                .addLore(CC.SECOND + CC.ITALIC + "Click to rename " + personal.getDisplayName())
                .build();
    }

    @Override
    public void onClick(Player player, ClickType clickType) {
        player.closeInventory();
        Tools.playSuccess(player);

        profile.setRenaming(true);
        profile.setEditingLoadout(personal);
        Scheduler.runLater(() -> profile.setSelectedLoadout(loadout), 1L);

        player.sendMessage(CC.SECOND + "Enter the new name for the loadout.");
    }
}
