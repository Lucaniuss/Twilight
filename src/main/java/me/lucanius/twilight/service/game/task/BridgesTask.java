package me.lucanius.twilight.service.game.task;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.cooldown.Cooldown;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.tools.CC;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Lucanius
 * @since May 26, 2022
 */
public class BridgesTask extends BukkitRunnable {

    private final Twilight plugin;
    private final Game game;
    private final int initial;

    private int current;

    public BridgesTask(Twilight plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
        this.initial = 6;

        this.current = initial;
    }

    @Override
    public void run() {
        if (current <= 1) {
            cancel();
        }

        if (current == initial) {
            game.getAliveMembers().forEach(member -> {
                Player player = member.getPlayer();
                player.teleport(member.getTeam().getSpawn());

                game.getLoadout().apply(player, member.getProfile());

                player.resetMaxHealth();
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                player.setLevel(0);
                player.setExp(0.0f);
                player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);

                Cooldown cooldown = plugin.getCooldowns().get(member.getUniqueId(), "BRIDGES");
                if (cooldown != null && cooldown.active()) {
                    cooldown.cancel();
                }
            });
        }

        current--;
        boolean cancelled = current <= 0;
        game.sendMessageWithSound(
                cancelled ? CC.SECOND + "The next round has started!" : CC.SECOND + "Starting next round " + CC.MAIN + current + CC.SECOND + "s...",
                cancelled ? Sound.FIREWORK_TWINKLE : Sound.NOTE_STICKS
        );
    }
}
