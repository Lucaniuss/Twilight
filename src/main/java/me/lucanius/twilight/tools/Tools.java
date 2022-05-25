package me.lucanius.twilight.tools;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.Optional;

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

    public void clearPlayer(Player player) {
        player.resetMaxHealth();
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setFireTicks(0);
        player.setFallDistance(0);

        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);

        PlayerInventory inventory = player.getInventory();
        inventory.setHeldItemSlot(0);
        inventory.clear();
        inventory.setArmorContents(null);
        player.updateInventory();

        Scheduler.run(() -> { // This can't be async, so we make sure this will not throw an exception
            player.setWalkSpeed(0.2f);
            player.setFlySpeed(0.2f);
            player.setGameMode(GameMode.SURVIVAL);
        });
    }

    public String getEnumName(String string) {
        String[] split = string.split("_");
        StringBuilder builder = new StringBuilder();
        for (String s : split) {
            builder.append(WordUtils.capitalize(s.toLowerCase())).append(" ");
        }

        String str = builder.toString();
        return Optional.of(str).filter(s -> s.length() != 0).map(s -> s.substring(0, s.length() - 1)).orElse(str);
    }
}
