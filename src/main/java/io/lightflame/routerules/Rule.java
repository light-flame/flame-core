package io.lightflame.routerules;

/**
 * Rule
 */

public interface Rule<E> {

    boolean isValid(E obj);
    RuleKind kind();
    int score();
}