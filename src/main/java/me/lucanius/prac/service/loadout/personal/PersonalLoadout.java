package me.lucanius.prac.service.loadout.personal;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@Data @AllArgsConstructor
public class PersonalLoadout {

    private final int i;

    private String kitName;
    private ItemStack[] contents;
    private String displayName;

}
