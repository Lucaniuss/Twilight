package me.lucanius.twilight.tools.functions.pair;

/**
 * @author Clouke
 * @since 25.05.2022 13:27
 * © Twilight - All Rights Reserved
 */
public class Pairs<L, R> implements Pair<L, R> {

    private final L left;
    private final R right;

    public Pairs(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public L getLeft() {
        return this.left;
    }

    @Override
    public R getRight() {
        return this.right;
    }
}
