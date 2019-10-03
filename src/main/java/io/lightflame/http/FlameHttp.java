package io.lightflame.http;

import java.util.*;

import io.lightflame.bootstrap.Flame;
import io.lightflame.routerules.HttpMethodRule;
import io.lightflame.routerules.HttpPathRule;
import io.lightflame.routerules.HttpPortRule;
import io.lightflame.routerules.HttpPrefixPathRule;
import io.lightflame.routerules.RouteRules;
import io.lightflame.routerules.RouteStore;
import io.lightflame.routerules.Rule;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;


/**
 * HttpBeanStore
 */

public class FlameHttp {

    static private Map<UUID, Flame> functionMap = new HashMap<>();
    static private RouteStore<FullHttpRequest> rs = new  RouteStore<>();

    private String prefix = "";
    private int port = 8080;
    private Flame custom404 = handler404();

    public FlameHttp() {
    }

    public FlameHttp(int port) {
        this.port = port;
    }

    public FlameHttp(String prefix) {
        this.prefix = prefix;
    }

    public FlameHttp(int port, String prefix) {
        this.prefix = prefix;
        this.port = port;
    }

    public FlameHttp add404Function(Flame f){
        this.custom404 = f;
        return this;
    }

    public BuildRoute R(){
        return new BuildRoute(this.port, this.prefix);
    }

    FlameHttpResponse runFunctionByRequest(FullHttpRequest request) throws Exception{
        Flame<FlameHttpContext, FlameHttpResponse> function = this.custom404;

        RouteRules<FullHttpRequest> routeRules = rs.getRouteRules(request);
        if (routeRules != null) {
            function = functionMap.get(routeRules.getKey());
        }

        FlameHttpContext ctx = new FlameHttpContext(request, new HttpUtils(), routeRules);
        return function.apply(ctx);
    }

    public class BuildRoute{

        private String prefix;
        private List<Rule<FullHttpRequest>> rules = new ArrayList<>();

        BuildRoute(Integer port, String prefix){
            this.prefix = prefix;
            if (port != null){
                rules.add(new HttpPortRule(port));
            }
        }

        private UUID addToStore(String url){
            rules.add((url.contains("*")) ? new HttpPrefixPathRule(this.prefix + url) : new HttpPathRule(this.prefix + url));
            return rs.addRouteRule(
                new RouteRules<FullHttpRequest>()
                    .addRules(rules)
            );
        }

        public BuildRoute methodRule(HttpMethod method){
            rules.add(new HttpMethodRule(method));
            return this;
        }

        public BuildRoute headerRule(String key, String value){
            return this;
        }

        public BuildRoute queryRule(String key, String value){
            return this;
        }     

        public BuildRoute pathRule(String key, String value){
            return this;
        }    

        public UUID httpALL(String url, Flame<FlameHttpContext, FlameHttpResponse> function){
            UUID key = this.addToStore(url);
            functionMap.put(key, function);
            return key;
        }    

        public UUID httpGET(String url, Flame<FlameHttpContext, FlameHttpResponse> function){
            rules.add(new HttpMethodRule(HttpMethod.GET));
            return httpALL(url, function);
        }
    
        public UUID httpPOST(String url, Flame<FlameHttpContext, FlameHttpResponse> function){
            rules.add(new HttpMethodRule(HttpMethod.POST));
            return httpALL(url, function);
        }

        public UUID httpPUT(String url, Flame<FlameHttpContext, FlameHttpResponse> function){
            rules.add(new HttpMethodRule(HttpMethod.PUT));
            return httpALL(url, function);
        }

        public UUID httpPATCH(String url, Flame<FlameHttpContext, FlameHttpResponse> function){
            rules.add(new HttpMethodRule(HttpMethod.PATCH));
            return httpALL(url, function);
        }
    }

    private Flame<FlameHttpContext, FlameHttpResponse> handler404() {
        return (ctx) -> new FlameHttpResponse(
            HttpResponseStatus.NOT_FOUND,
            Unpooled.wrappedBuffer("nothing here.. =(".getBytes())
        );
    }
    
}