package me.lucanius.twilight.service.loadout.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucanius.twilight.service.loadout.type.callable.LoadoutTypeCallable;

import java.util.Arrays;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
@Getter @AllArgsConstructor // TODO: Implement the callable
public enum LoadoutType {

    NONE(((victim, killer) -> {})),
    SUMO(((victim, killer) -> {})),
    BOXING(((victim, killer) -> {})),
    BRIDGES(((victim, killer) -> {}));

    private final LoadoutTypeCallable callable;

    public static LoadoutType getOrDefault(String input) {
        return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(input)).findFirst().orElse(NONE);
    }
}
