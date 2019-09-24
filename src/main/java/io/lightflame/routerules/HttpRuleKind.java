package io.lightflame.routerules;

/**
 * HttpRuleKind
 */
public enum HttpRuleKind implements RuleKind{
    METHOD,
    PATH,
    PREFIX,
    QPARAM,
    HEADER,
    PORT
}