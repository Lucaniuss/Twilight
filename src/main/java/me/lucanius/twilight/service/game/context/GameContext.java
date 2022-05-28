package me.lucanius.twilight.service.game.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Getter @AllArgsConstructor
public enum GameContext {

    NORMAL("Normal", false, 0, Material.IRON_SWORD),
    FFA("Ffa", true, 2, Material.BLAZE_POWDER),
    SPLIT("Split", true, 6, Material.QUARTZ);

    private final String name;
    private final boolean party;
    private final int slot;
    private final Material material;

}
