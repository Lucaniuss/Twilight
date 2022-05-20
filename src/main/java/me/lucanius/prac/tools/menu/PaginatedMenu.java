package me.lucanius.prac.tools.menu;

import lombok.Getter;
import me.lucanius.prac.tools.menu.pagination.buttons.PageButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * I don't know who the author is.
 * @edited Lucanius
 */
public abstract class PaginatedMenu extends Menu {

    @Getter private int page = 1;

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getTitle(Player player) {
        return this.getPrePaginatedTitle(player);
    }

    @Override
    public final Map<Integer, Button> getButtons(Player player) {
        int maxItemsPerPage = getMaxItemsPerPage(player);

        int minIndex = (int) ((double) (this.page - 1) * maxItemsPerPage);
        int maxIndex = (int) ((double) (this.page) * maxItemsPerPage);

        final HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new PageButton(-1, this));
        buttons.put(8, new PageButton(1, this));

        fillBottomAndTop(buttons);

        for (Map.Entry<Integer, Button> entry : this.getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();
            if (ind >= minIndex && ind < maxIndex) {
                ind -= (int) ((double) (maxItemsPerPage) * (this.page - 1)) - 9;
                buttons.put(ind, entry.getValue());
            }
        }

        Map<Integer, Button> global = this.getGlobalButtons(player);
        if (global != null) {
            buttons.putAll(global);
        }

        return buttons;
    }

    public int getMaxItemsPerPage(Player player) {
        return 18;
    }

    public abstract String getPrePaginatedTitle(Player player);

    public abstract Map<Integer, Button> getAllPagesButtons(Player player);

    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    public final void modPage(Player player, int mod) {
        this.page += mod;
        this.getButtons().clear();
        this.open(player);
    }

    public final int getPages(Player player) {
        int buttonAmount = this.getAllPagesButtons(player).size();
        if (buttonAmount == 0) {
            return 1;
        }

        return (int) Math.ceil(buttonAmount / (double) this.getMaxItemsPerPage(player));
    }

    private void fillBottomAndTop(Map<Integer, Button> buttons) {
        final int size = getSize();
        IntStream.range(0, size)
                .filter(slot -> buttons.get(slot) == null)
                .filter(slot -> slot < 9 || slot > size - 10)
                .forEach(slot -> buttons.putIfAbsent(slot, this.glass()));
    }
}
