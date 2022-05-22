package me.lucanius.twilight.tools.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ThatKawaiiSam from his Assemble project
 * @edited Lucanius
 */
@Getter @AllArgsConstructor
public enum BoardStyle {

    KOHI(true, 15),
    VIPER(true, -1),
    MODERN(false, 1),
    CUSTOM(false, 0);

    private boolean descending;
    private int startNumber;

    public BoardStyle reverse() {
        return descending(!this.descending);
    }

    public BoardStyle descending(boolean descending) {
        this.descending = descending;
        return this;
    }

    public BoardStyle startNumber(int startNumber) {
        this.startNumber = startNumber;
        return this;
    }
}
