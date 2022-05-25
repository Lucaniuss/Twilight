package me.lucanius.twilight.layout.provider;

import me.lucanius.twilight.Twilight;
import me.lucanius.twilight.service.game.Game;
import me.lucanius.twilight.service.profile.modules.GameProfile;
import me.lucanius.twilight.tools.CC;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
public abstract class AbstractProvider {

    protected final Twilight plugin = Twilight.getInstance();

    public Collection<String> getBoxing(GameProfile fProfile, GameProfile eProfile) {
        Collection<String> lines = new ArrayList<>();
        int fHits = fProfile.getHits(), eHits = eProfile.getHits(), fCombo = fProfile.getCombo(), eCombo = eProfile.getCombo();

        lines.add(" ");
        lines.add(CC.MAIN + "Hits: " + CC.SECOND + (fHits - eHits < eHits - fHits ? "&c(" + (fHits - eHits) + ")" : "&a(+" + (fHits - eHits) + ")"));
        lines.add(" " + CC.WHITE + "You: " + CC.MAIN + fHits + " " + CC.SECOND + (fCombo > 1 ? "(" + fCombo + " Combo)" : ""));
        lines.add(" " + CC.WHITE + "Them: " + CC.MAIN + eHits + " " + CC.SECOND + (eCombo > 1 ? "(" + eCombo + " Combo)" : ""));

        return lines;
    }

    public Collection<String> getBridges(Game game) {
        Collection<String> lines = new ArrayList<>();

        lines.add(" ");
        game.getTeams().forEach(team -> {
            StringBuilder builder = new StringBuilder();
            builder.append(team.getPrefix());
            for (int i = 0; i < 5; i++) {
                builder.append(i >= team.getPoints() ? "&7" : team.getColor().toString()).append("⬤");
            }
            lines.add(builder.toString());
        });

        return lines;
    }
}
