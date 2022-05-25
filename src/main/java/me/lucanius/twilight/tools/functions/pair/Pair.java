package me.lucanius.twilight.tools.functions.pair;

/**
 * @author Clouke
 * @since 25.05.2022 13:26
 * © Twilight - All Rights Reserved
 */
public interface Pair<L, R> {

    L getLeft();

    R getRight();

    default void setLeft(L left) {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    default void setRight(R right) {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

}
