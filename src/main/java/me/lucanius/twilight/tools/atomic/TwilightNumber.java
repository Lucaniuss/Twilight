package me.lucanius.twilight.tools.atomic;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Lucanius
 * @since June 04, 2022.
 * Twilight - All Rights Reserved.
 */
@NoArgsConstructor @AllArgsConstructor
public class TwilightNumber extends Number {

    private int value = 0;

    public int get() {
        return value;
    }

    public int set(int i) {
        return value = i;
    }

    public int getAndIncrease() {
        return value++;
    }

    public int increaseAndGet() {
        return ++value;
    }

    public int getAndDecrease() {
        return value--;
    }

    public int decreaseAndGet() {
        return --value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }
}
