package io.lightflame.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class FlameHttpStore {

    static private Map<String, Flame> functionMap = new HashMap<>();
    static private RouteStore<FullHttpRequest> rs = new HttpRouteStore();

    private String prefix = "";
    private int port = 8080;
    private Flame custom404 = handler404();

    public FlameHttpStore() {
    }

    public FlameHttpStore(int port) {
        this.port = port;
    }

    public FlameHttpStore(String prefix) {
        this.prefix = prefix;
    }

    public FlameHttpStore(int port, String prefix) {
        this.prefix = prefix;
        this.port = port;
    }

    public FlameHttpStore add404Function(Flame f){
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

        private String addToStore(String url){
            rules.add((url.contains("*")) ? new HttpPrefixPathRule(this.prefix + url) : new HttpPathRule(this.prefix + url));
            return rs.addRouteRule(
                new RouteRules<FullHttpRequest>()
                    .addRules(rules)
            );
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

        public void httpALL(String url, Flame<FlameHttpContext, FlameHttpResponse> function){
        }    

        public void httpGET(String url, Flame<FlameHttpContext, FlameHttpResponse> function){
            rules.add(new HttpMethodRule(HttpMethod.GET));
            functionMap.put(this.addToStore(url), function);
        }
    
        public void httpPOST(String url, Flame<FlameHttpContext, FlameHttpResponse> function){
            rules.add(new HttpMethodRule(HttpMethod.POST));
    
            functionMap.put(this.addToStore(url), function);
        }
    }

    public FlameHttpStore addHeaderRyle(){
        return this;
    }



    private Flame<FlameHttpContext, FlameHttpResponse> handler404() {
        return (ctx) -> new FlameHttpResponse(
            HttpResponseStatus.NOT_FOUND,
            Unpooled.wrappedBuffer("nothing here.. =(".getBytes())
        );
    }
    
}