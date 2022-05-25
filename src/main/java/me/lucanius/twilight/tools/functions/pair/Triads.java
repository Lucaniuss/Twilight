package me.lucanius.twilight.tools.functions.pair;

/**
 * @author Clouke
 * @since 25.05.2022 13:30
 * © Twilight - All Rights Reserved
 */
public class Triads<A, B, C> implements Triad<A, B, C> {

    private final A a;
    private final B b;
    private final C c;

    public Triads(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public A getA() {
        return this.a;
    }

    @Override
    public B getB() {
        return this.b;
    }

    @Override
    public C getC() {
        return this.c;
    }
}
