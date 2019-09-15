package io.lightflame.routerules;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * RuleUtil
 */
public class RouteStore<E> {

    static private List<RouteRules> routeRules = new ArrayList<>();

    public String addRouteRule(RouteRules routeRule){
        if (routeRule.getKey() == null){
            routeRule.setKey(UUID.randomUUID().toString());
        }
        routeRules.add(routeRule);
        return routeRule.getKey();
    }

    public RouteRules getRouteRules(E income, StoreKind store){
        Optional<RouteRules> optRule =  routeRules
            .stream()
            .filter(x -> x.getStore().equals(store))
            .filter(x -> x.match(income))
            .max(Comparator.comparing((RouteRules r) -> r.score()));
        if (!optRule.isPresent()){
            return null;
        }
        return optRule.get();    
    }
}