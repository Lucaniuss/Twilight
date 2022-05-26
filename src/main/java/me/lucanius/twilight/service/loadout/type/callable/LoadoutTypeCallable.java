package me.lucanius.twilight.service.loadout.type.callable;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.game.Game;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public interface LoadoutTypeCallable {

    void execute(Twilight plugin, Player victim, Player killer, Game game);

}
