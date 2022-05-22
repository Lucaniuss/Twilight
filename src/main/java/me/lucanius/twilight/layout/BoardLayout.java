package me.lucanius.twilight.layout;

import lombok.RequiredArgsConstructor;
import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.board.BoardAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
@RequiredArgsConstructor
public class BoardLayout implements BoardAdapter {

    private final Twilight plugin;

    @Override
    public String getTitle(Player player) {
        return CC.MAIN + CC.BOLD + "Twilight";
    }

    @Override
    public List<String> getLines(Player player) {
        final List<String> lines = new ArrayList<>();

        lines.add(CC.SMALL_BAR);
        lines.add("&fOnline: " + CC.SECOND + plugin.getOnline().size());
        lines.add(" ");
        lines.add("&7&olucanius.me");
        lines.add(CC.SMALL_BAR);

        return lines;
    }
}
