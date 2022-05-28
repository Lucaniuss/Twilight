package me.lucanius.twilight.service.party.menus.other.buttons;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.service.party.Party;
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
public class OtherPartiesButton extends Button {

    private final Party party;

    @Override
    public ItemStack getItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM);
        Player leader = party.getLeaderPlayer();

        builder.setData(3);
        builder.setOwner(leader.getName());
        builder.setName(CC.MAIN + leader.getName() + "'s " + CC.SECOND + "Party");
        builder.addLore(CC.SMALL_BAR);
        party.toPlayers().forEach(member -> builder.addLore(CC.ICON + CC.SECOND + member.getName()));
        builder.addLore(" ");
        builder.addLore(CC.SECOND + CC.ITALIC + "Click to duel");
        builder.addLore(CC.SMALL_BAR);

        return builder.build();
    }

    @Override
    public void onClick(Player player, ClickType clickType) {
        player.performCommand("duel " + party.getLeaderPlayer().getName());
        Tools.playSuccess(player);

        Scheduler.runLater(player::closeInventory, 1L);
    }
}
