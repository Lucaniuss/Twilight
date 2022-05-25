package me.lucanius.twilight.service.damage;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Data @AllArgsConstructor
public class CachedDamage {

    private Player damager;
    private long timeStamp;

}
