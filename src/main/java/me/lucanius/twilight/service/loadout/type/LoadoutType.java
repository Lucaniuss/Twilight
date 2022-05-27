package me.lucanius.twilight.service.loadout.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucanius.twilight.event.events.GameEndEvent;
import me.lucanius.twilight.service.cooldown.Cooldown;
import me.lucanius.twilight.service.game.context.GameState;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.loadout.type.callable.LoadoutTypeCallable;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Scheduler;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@Getter @AllArgsConstructor
public enum LoadoutType {

    NONE(((plugin, victim, killer, game) -> {
        boolean hasKiller = killer != null;
        if (hasKiller) {
            Scheduler.run(() -> killer.hidePlayer(victim));
        }
        if (game.getState() == GameState.TERMINATED) {
            return;
        }

        UUID victimId = victim.getUniqueId();
        GameTeam victimTeam = game.getTeam(victimId);
        GameTeam killerTeam = game.getOpposingTeam(victimTeam);
        if (hasKiller) {
            game.sendMessage(victimTeam.getColor() + victim.getName() + CC.SECOND + " was killed by " + killerTeam.getColor() + killer.getName() + CC.SECOND + ".");
        } else {
            game.sendMessage(victimTeam.getColor() + victim.getName() + CC.SECOND + " died.");
        }

        if (victim.isOnline()) {
            game.addSpectator(victim, true);
        }

        victimTeam.killSpecific(victimId);
        if (!(victimTeam.getAliveSize() > 0)) {
            new GameEndEvent(game, killerTeam, killerTeam);
        }
    })),
    SUMO(((plugin, victim, killer, game) -> {
        NONE.getCallable().execute(plugin, victim, killer, game);
    })),
    BOXING(((plugin, victim, killer, game) -> {
        NONE.getCallable().execute(plugin, victim, killer, game);
    })),
    BRIDGES(((plugin, victim, killer, game) -> {
        boolean hasKiller = killer != null;

        UUID victimId = victim.getUniqueId();
        GameTeam victimTeam = game.getTeam(victimId);
        GameTeam killerTeam = game.getOpposingTeam(victimTeam);
        if (hasKiller) {
            game.sendMessage(victimTeam.getColor() + victim.getName() + CC.SECOND + " was killed by " + killerTeam.getColor() + killer.getName() + CC.SECOND + ".");
        } else {
            game.sendMessage(victimTeam.getColor() + victim.getName() + CC.SECOND + " died.");
        }

        game.sendSoundToTeam(killerTeam, Sound.ORB_PICKUP);
        victim.teleport(victimTeam.getSpawn());

        game.getLoadout().apply(victim, plugin.getProfiles().get(victimId));

        victim.playSound(victim.getLocation(), Sound.HURT_FLESH, 10f, 1f);

        victim.resetMaxHealth();
        victim.setHealth(victim.getMaxHealth());
        victim.setFoodLevel(20);
        victim.setLevel(0);
        victim.setExp(0.0f);
        victim.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(victim::removePotionEffect);

        Cooldown cooldown = plugin.getCooldowns().get(victimId, "BRIDGES");
        if (cooldown != null && cooldown.active()) {
            cooldown.cancel();
        }
    }));

    private final LoadoutTypeCallable callable;

    public static LoadoutType getOrDefault(String input) {
        return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(input)).findFirst().orElse(NONE);
    }
}
