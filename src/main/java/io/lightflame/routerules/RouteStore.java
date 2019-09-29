package io.lightflame.routerules;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * RuleUtil
 */
abstract public class RouteStore<E> {

    private List<RouteRules<E>> routeRules = new ArrayList<>();

    public UUID addRouteRule(RouteRules<E> routeRule){
        if (routeRule.getKey() == null){
            routeRule.setKey(UUID.randomUUID());
        }
        routeRules.add(routeRule);
        return routeRule.getKey();
    }

    public RouteRules<E> getRouteRules(E income){
        Optional<RouteRules<E>> optRule =  this.routeRules
            .stream()
            .filter(x -> x.match(income))
            .max(Comparator.comparing((RouteRules<E> r) -> r.score()));
        if (!optRule.isPresent()){
            return null;
        }
        return optRule.get();    
    }

    public RouteStore<E> addRuleToStore(String key, Rule<E> rule){
        Optional<RouteRules<E>> optRule =  this.routeRules
            .stream()
            .filter(x -> x.getKey().equals(key))
            .findFirst();
        if (optRule.isPresent()){
            optRule.get().addRule(rule);
        }
        return this;
    }

}