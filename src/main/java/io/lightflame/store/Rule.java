package io.lightflame.store;

import java.util.List;

import io.lightflame.store.HttpRouteRules.RuleEnum;

/**
 * Rule
 */
public interface Rule {
    Boolean validate(List<String> s);
    RuleEnum kind();
    int score();
}