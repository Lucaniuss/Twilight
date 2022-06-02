package me.lucanius.twilight.service.profile.modules.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lucanius
 * @since June 01, 2022.
 * Twilight - All Rights Reserved.
 */
@Getter @AllArgsConstructor
public enum StatContext {

    WINS(0, "wins"),
    LOSSES(0, "losses"),
    ELO(1000, "elo");

    private final int defaultValue;
    private final String databaseKey;

}
