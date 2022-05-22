package me.lucanius.twilight.tools.board;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author ThatKawaiiSam from his Assemble project
 * @edited Lucanius
 */
public interface BoardAdapter {

    String getTitle(Player player);

    List<String> getLines(Player player);

}
