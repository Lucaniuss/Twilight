package me.lucanius.twilight.service.loadout.type.callable;

import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public interface LoadoutTypeCallable {

    void execute(Player victim, Player killer);

}
