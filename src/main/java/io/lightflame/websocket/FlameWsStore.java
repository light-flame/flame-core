package io.lightflame.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.lightflame.bootstrap.Flame;
import io.lightflame.routerules.RouteRules;
import io.lightflame.routerules.RouteStore;
import io.lightflame.routerules.Rule;
import io.lightflame.routerules.WsPathRule;

/**
 * FlameWebSocketStore
 */
public class FlameWsStore {

    static private RouteStore<WsRequestWrapper> rs = new WsRouteStore();
    static private Map<String, Flame<FlameWsContext, FlameWsContext>> functionMap = new HashMap<>();

    FlameWsContext runFunctionByRequest(WsRequestWrapper wrapper) throws Exception{

        RouteRules<WsRequestWrapper> routeRules = rs.getRouteRules(wrapper);
        if (routeRules == null) {
            return null;
        }
        Flame<FlameWsContext,FlameWsContext> function = functionMap.get(routeRules.getKey());

        FlameWsContext ctx = new FlameWsContext(wrapper.getRequest());
        return function.apply(ctx);
    }

    public BuildRoute R(){
        return new BuildRoute();
     }

    public class BuildRoute{

        private List<Rule<WsRequestWrapper>> rules = new ArrayList<>();

        private String addToStore(String url){
            rules.add(new WsPathRule(url));
            return rs.addRouteRule(
                new RouteRules<WsRequestWrapper>()
                    .addRules(rules)
            );
        }

        public void path(String url, Flame function){
            functionMap.put(this.addToStore(url), function);
        }
    }
}