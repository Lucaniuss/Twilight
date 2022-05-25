package me.lucanius.twilight.tools.date;

import lombok.experimental.UtilityClass;

/**
 * @author Lucanius
 * @since May 25, 2022
 */
@UtilityClass
public final class DateTools {

    public String formatIntToMMSS(int secs) {
        int seconds = secs % 60;
        secs -= seconds;
        long minutesCount = secs / 60;
        long minutes = minutesCount % 60L;
        minutesCount -= minutes;
        long hours = minutesCount / 60L;
        final StringBuilder result = new StringBuilder();
        result.setLength(0);
        if (hours > 0L) {
            if (hours < 10L) {
                result.append("0");
            }
            result.append(hours);
            result.append(":");
        }
        if (minutes < 10L) {
            result.append("0");
        }
        result.append(minutes);
        result.append(":");
        if (seconds < 10) {
            result.append("0");
        }
        result.append(seconds);
        return result.toString();
    }
}
