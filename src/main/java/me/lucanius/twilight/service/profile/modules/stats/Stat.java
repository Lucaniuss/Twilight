package me.lucanius.twilight.service.profile.modules.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Lucanius
 * @since June 01, 2022.
 * Twilight - All Rights Reserved.
 */
@Data @AllArgsConstructor
public class Stat {

    private final StatContext context;
    private final String loadoutName;

    private int value;

    public Stat(StatContext context, String loadoutName) {
        this.context = context;
        this.loadoutName = loadoutName;
        this.value = context.getDefaultValue();
    }
}
