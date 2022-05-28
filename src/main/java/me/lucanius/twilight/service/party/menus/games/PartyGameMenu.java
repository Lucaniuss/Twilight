package me.lucanius.twilight.service.party.menus.games;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.service.game.context.GameContext;
import me.lucanius.twilight.service.party.Party;
import me.lucanius.twilight.service.party.menus.games.buttons.PartyGameButton;
import me.lucanius.twilight.tools.menu.Button;
import me.lucanius.twilight.tools.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lucanius
 * @since May 27, 2022
 */
@RequiredArgsConstructor
public class PartyGameMenu extends Menu {

    private final Party party;

    @Override
    public String getTitle(Player player) {
        return "Party Games";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Arrays.stream(GameContext.values()).filter(GameContext::isParty).forEach(context ->
                buttons.put(context.getSlot(), new PartyGameButton(party, context))
        );

        fillEmptyWithGlass(buttons);

        return buttons;
    }
}
