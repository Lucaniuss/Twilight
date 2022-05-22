package me.lucanius.twilight.tools;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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

    public EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public int getPing(Player player) {
        return getEntityPlayer(player).ping;
    }

    public void sendPacket(Player player, Packet<?> packet) {
        getEntityPlayer(player).playerConnection.sendPacket(packet);
    }
}
