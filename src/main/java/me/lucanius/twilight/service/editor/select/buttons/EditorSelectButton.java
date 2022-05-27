package me.lucanius.twilight.service.editor.select.buttons;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.service.editor.overview.EditorOverviewMenu;
import me.lucanius.twilight.service.loadout.Loadout;
import me.lucanius.twilight.service.loadout.type.LoadoutType;
import me.lucanius.twilight.service.profile.modules.EditorProfile;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Tools;
import me.lucanius.twilight.tools.item.ItemBuilder;
import me.lucanius.twilight.tools.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
@RequiredArgsConstructor
public class EditorSelectButton extends Button {

    private final Loadout loadout;
    private final EditorProfile profile;

    @Override
    public ItemStack getItem(Player player) {
        String name = loadout.getName();
        ItemBuilder builder = new ItemBuilder(loadout.getIcon().clone());
        int amount = profile.size(name);

        builder.setName(CC.MAIN + CC.BOLD + name);
        builder.addLore(CC.MEDIUM_BAR);
        builder.addLore(CC.ICON + CC.WHITE + "Your Loadouts: " + (amount == 0 ? CC.RED + "None" : CC.SECOND + amount));
        builder.addLore(" ");
        builder.addLore(CC.SECOND + CC.ITALIC + "Click to edit");
        builder.addLore(CC.MEDIUM_BAR);

        builder.setAmount(amount == 0 ? 1 : amount);

        return builder.build();
    }

    @Override
    public void onClick(Player player, ClickType clickType) {
        if (loadout.getType() == LoadoutType.SUMO) {
            Tools.playFail(player);
            player.sendMessage(CC.RED + "This loadout cannot be edited!");
            return;
        }

        Tools.playSuccess(player);
        profile.setSelectedLoadout(loadout);
        new EditorOverviewMenu(loadout, profile).open(player);
    }
}
