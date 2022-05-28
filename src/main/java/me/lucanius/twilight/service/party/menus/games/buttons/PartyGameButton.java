package me.lucanius.twilight.service.party.menus.games.buttons;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.service.game.context.GameContext;
import me.lucanius.twilight.service.party.Party;
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
public class PartyGameButton extends Button {

    private final Party party;
    private final GameContext context;

    @Override
    public ItemStack getItem(Player player) {
        ItemBuilder builder = new ItemBuilder(context.getMaterial());

        builder.setName(CC.MAIN + CC.BOLD + context.getName());
        builder.addLore(CC.SECOND + CC.ITALIC + "Click to play");

        return builder.build();
    }

    @Override
    public void onClick(Player player, ClickType clickType) {
        if (!(party.getMembers().size() > 1)) {
            player.sendMessage(CC.RED + "You need at least 2 players to play this game.");
            Tools.playFail(player);
            return;
        }

        // TODO: Implement
    }
}
