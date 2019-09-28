package io.lightflame.bootstrap;

import java.util.Objects;

@FunctionalInterface
public interface Flame<T, R> {
    R apply(T ctx) throws Exception;

    default <V> Flame<T, V> and(Flame<R, V> after) {
        Objects.requireNonNull(after);
        return (t) -> after.apply(this.apply(t));
    }
}
