package io.lightflame.routerules;

/**
 * Rule
 */
public interface Rule<C,E> {

    boolean isValid(E obj);
    RuleKind kind();
    void setParam(C obj);
    int score();
}