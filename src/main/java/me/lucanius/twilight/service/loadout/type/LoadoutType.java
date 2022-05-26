package me.lucanius.twilight.service.loadout.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucanius.twilight.event.events.GameEndEvent;
import me.lucanius.twilight.service.game.context.GameState;
import me.lucanius.twilight.service.game.team.GameTeam;
import me.lucanius.twilight.service.loadout.type.callable.LoadoutTypeCallable;
import me.lucanius.twilight.tools.CC;
import me.lucanius.twilight.tools.Scheduler;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@Getter @AllArgsConstructor
public enum LoadoutType {

    NONE(((victim, killer, game) -> {
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
    SUMO(((victim, killer, game) -> {
        NONE.getCallable().execute(victim, killer, game);
    })),
    BOXING(((victim, killer, game) -> {
        NONE.getCallable().execute(victim, killer, game);
    })),
    BRIDGES(((victim, killer, game) -> {

    }));

    private final LoadoutTypeCallable callable;

    public static LoadoutType getOrDefault(String input) {
        return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(input)).findFirst().orElse(NONE);
    }
}
