package me.lucanius.twilight.service.queue.callback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucanius.twilight.tools.CC;

/**
 * @author Lucanius
 * @since May 22, 2022
 */
@Getter @AllArgsConstructor
public enum QueueCallback {

    NONE(""),
    ALLOWED(CC.GREEN + "Starting game..."),
    DENIED(CC.RED + "An error occurred while attempting to start a game."),
    NO_ARENA(CC.RED + "There are no arenas available at this moment."),
    NO_LOADOUT(CC.RED + "There was an error while retrieving the loadout, Contact an administrator.");

    private final String message;

}
