package io.lightflame.websocket;

import java.util.*;

import io.lightflame.bootstrap.Flame;
import io.lightflame.routerules.*;
import io.netty.channel.ChannelHandlerContext;

/**
 * FlameWebSocketStore
 */
public class FlameWs {

    private int port = 8080;

    public FlameWs(int port){
        this.port = port;
    }

    public FlameWs(){

    }

    static private Session session = new Session();
    static private RouteStore<ChannelHandlerContext> rs = new RouteStore<>();
    static private Map<UUID, Flame<FlameWsContext, FlameWsContext>> functionMap = new HashMap<>();

    FlameWsContext runFunctionByRequest(ChannelHandlerContext ctx) throws Exception{

        RouteRules<ChannelHandlerContext> routeRules = rs.getRouteRules(ctx);
        if (routeRules == null) {
            return null;
        }
        Flame<FlameWsContext,FlameWsContext> function = functionMap.get(routeRules.getKey());
        return function.apply(new FlameWsContext(ctx, session));
    }

    void addChannelToSession(String key, ChannelHandlerContext ch){
        session.addSession(key, ch);
    }

    public BuildRoute R(){
        return new BuildRoute(this.port);
     }

    public class BuildRoute{

        private int port;

        BuildRoute(int port){
            this.rules.add(new WsPortRule(port));
        }

        private List<Rule<ChannelHandlerContext>> rules = new ArrayList<>();

        private UUID addToStore(String url){
            rules.add(new WsPathRule(url));
            return rs.addRouteRule(
                new RouteRules<ChannelHandlerContext>()
                    .addRules(rules)
            );
        }

        public UUID path(String url, Flame<FlameWsContext,FlameWsContext> function){
            UUID k = this.addToStore(url);
            functionMap.put(k, function);
            return k;
        }

    }
}