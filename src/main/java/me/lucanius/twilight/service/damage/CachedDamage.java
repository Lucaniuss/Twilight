package me.lucanius.twilight.service.damage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CachedDamage {

    private Player damager;
    private long timeStamp;

}
