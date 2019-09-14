package io.lightflame.routerules;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * RuleUtil
 */
public class RouteStore<C,E> {

    List<RouteRules<C,E>> routeRules = new ArrayList<>();

    public String addRouteRule(RouteRules<C,E> routeRule){
        if (routeRule.getKey() == null){
            routeRule.setKey(UUID.randomUUID().toString());
        }
        this.routeRules.add(routeRule);
        return routeRule.getKey();
    }

    public RouteRules<C,E> getRouteRules(E income, StoreKind store){
        Optional<RouteRules<C,E>> optRule =  routeRules
            .stream()
            .filter(x -> x.getStore().equals(store))
            .filter(x -> x.match(income))
            .max(Comparator.comparing((RouteRules<C,E> r) -> r.score()));
        if (!optRule.isPresent()){
            return null;
        }
        return optRule.get();    
    }
}