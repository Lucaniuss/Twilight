package me.lucanius.twilight.service.profile.modules;

import lombok.Data;
import me.lucanius.twilight.service.game.team.GameTeam;

/**
 * @author Lucanius
 * @since May 25, 2022
 *
 * Used to cache all current game stats for each profile
 */
@Data
public class GameProfile {

    private int missedPots = 0;
    private int thrownPots = 0;
    private int longestCombo = 0;
    private int combo = 0;
    private int hits = 0;
    private int kills = 0;
    private boolean respawning = false;
    private GameTeam team;

    public void reset() {
        missedPots = 0;
        thrownPots = 0;
        longestCombo = 0;
        combo = 0;
        hits = 0;
        kills = 0;
        respawning = false;
        team = null;
    }

    public void throwPotion(boolean missed) {
        thrownPots++;
        if (missed) {
            missedPots++;
        }
    }

    public void giveDamage() {
        combo++;
        hits++;
        if (combo > longestCombo) {
            longestCombo = combo;
        }
    }

    public void getDamage() {
        combo = 0;
    }

    public void kill() {
        kills++;
        combo = 0;
    }

    public String getAccuracy() {
        return (thrownPots == 0 ? "N/A"
                : missedPots == 0 ? "100%"
                : thrownPots == missedPots ? "50%"
                : Math.round(100.0d - ((double) missedPots / (double) thrownPots) * 100.0d) + "%"
        );
    }
}
