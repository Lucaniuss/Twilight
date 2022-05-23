package me.lucanius.twilight.tools.functions;

import lombok.RequiredArgsConstructor;

/**
 * @author Lucanius, Clouke
 * @since May 23, 2022
 */
@RequiredArgsConstructor
public class Condition {

    private final boolean condition;

    public static Condition of(boolean condition, Runnable runnable) {
        final Condition con = new Condition(condition);
        if (condition) {
            runnable.run();
        }

        return con;
    }

    public Condition orElse(Runnable runnable) {
        if (!condition) {
            runnable.run();
        }

        return this;
    }
}
