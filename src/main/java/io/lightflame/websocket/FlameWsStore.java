package io.lightflame.websocket;

import java.util.HashMap;
import java.util.Map;

import io.lightflame.routerules.RouteRules;
import io.lightflame.routerules.RouteStore;

/**
 * FlameWebSocketStore
 */
public class FlameWsStore {

    static private RouteStore<WsRequestWrapper> rs = new WsRouteStore();
    static private Map<String, FlameWsFunction> functionMap = new HashMap<>();

    FlameWsContext runFunctionByRequest(WsRequestWrapper wrapper) throws Exception{

        RouteRules<WsRequestWrapper> routeRules = rs.getRouteRules(wrapper);
        if (routeRules == null) {
            return null;
        }
        FlameWsFunction function = functionMap.get(routeRules.getKey());

        FlameWsContext ctx = new FlameWsContext(wrapper.getRequest());
        return function.chain(ctx);
    }
}