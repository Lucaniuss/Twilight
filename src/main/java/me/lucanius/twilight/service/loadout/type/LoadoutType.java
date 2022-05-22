package me.lucanius.twilight.service.loadout.type;

import java.util.Arrays;

/**
 * @author Lucanius
 * @since May 20, 2022
 */
public enum LoadoutType {

    NONE,
    SUMO,
    BOXING;

    public static LoadoutType getOrDefault(String input) {
        return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(input)).findFirst().orElse(NONE);
    }
}
