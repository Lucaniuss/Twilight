package me.lucanius.prac.tools.board;

import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * @author ThatKawaiiSam from his Assemble project
 * @edited Lucanius
 */
public class BoardEntry {

    private final BoardManager board;

    private Team team;
    @Setter private String text, identifier;

    public BoardEntry(BoardManager board, String text, int position) {
        this.board = board;
        this.text = text;
        this.identifier = this.board.getUniqueIdentifier(position);

        this.setup();
    }

    public void setup() {
        final Scoreboard scoreboard = this.board.getScoreboard();
        if (scoreboard == null) {
            return;
        }

        String teamName = this.identifier;
        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }

        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }

        if (!team.getEntries().contains(this.identifier)) {
            team.addEntry(this.identifier);
        }

        if (!this.board.getEntries().contains(this)) {
            this.board.getEntries().add(this);
        }

        this.team = team;
    }

    public void send(int position) {
        final String[] split = splitTeamText(text);

        this.team.setPrefix(split[0]);
        this.team.setSuffix(split[1]);

        this.board.getObjective().getScore(this.identifier).setScore(position);
    }

    public void remove() {
        this.board.getIdentifiers().remove(this.identifier);
        this.board.getScoreboard().resetScores(this.identifier);
    }

    private String[] splitTeamText(String input) {
        final int inputLength = input.length();
        if (inputLength > 16) {
            String prefix = input.substring(0, 16);

            final int lastColorIndex = prefix.lastIndexOf(ChatColor.COLOR_CHAR);

            String suffix;

            if (lastColorIndex >= 14) {
                prefix = prefix.substring(0, lastColorIndex);
                suffix = ChatColor.getLastColors(input.substring(0, 17)) + input.substring(lastColorIndex + 2);
            } else {
                suffix = ChatColor.getLastColors(prefix) + input.substring(16);
            }

            if (suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }

            return new String[]{prefix, suffix};
        } else {
            return new String[]{input, ""};
        }
    }
}
