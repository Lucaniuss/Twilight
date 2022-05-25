package me.lucanius.twilight.service.lobby.hotbar;

import lombok.Data;
import me.lucanius.twilight.service.lobby.hotbar.context.HotbarContext;
import me.lucanius.twilight.service.lobby.hotbar.context.HotbarType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 23, 2022
 */
@Data
public class HotbarItem {

    private final ItemStack item;
    private final int slot;
    private final HotbarContext context;
    private final HotbarType type;

}
