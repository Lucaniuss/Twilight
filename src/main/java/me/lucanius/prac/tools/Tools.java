package me.lucanius.prac.tools;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@UtilityClass
public final class Tools {

    public boolean isAsync() {
        return !Bukkit.isPrimaryThread();
    }

    public void playRandom(Sound sound, Player player) {
        player.playSound(player.getLocation(), sound, 1f, (float) (Math.random() / 2) + 1f);
    }

    public void playFail(Player player) {
        playRandom(Sound.VILLAGER_NO, player);
    }

    public void playSuccess(Player player) {
        playRandom(Sound.CLICK, player);
    }

    public boolean isInt(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (final Exception ignored) {
            return false;
        }
    }

    public boolean isDouble(String d) {
        try {
            Double.parseDouble(d);
            return true;
        } catch (final Exception ignored) {
            return false;
        }
    }

    public void log(String str) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(CC.MAIN + "[Twilight] &f" + str));
    }
}
