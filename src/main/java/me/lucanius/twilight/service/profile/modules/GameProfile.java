package me.lucanius.twilight.service.profile.modules;

import lombok.Data;

/**
 * @author Lucanius
 * @since May 25, 2022
 * Used to cache all GameStats for profile
 */
@Data
public class GameProfile {

    private int missedPots = 0;
    private int thrownPots = 0;
    private int longestCombo = 0;
    private int combo = 0;
    private int hits = 0;
    private int kills = 0;
    private int lives = 0;
    private boolean respawning = false;

    public void reset() {
        this.missedPots = 0;
        this.thrownPots = 0;
        this.longestCombo = 0;
        this.combo = 0;
        this.hits = 0;
        this.kills = 0;
        this.lives = 0;
        this.respawning = false;
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
