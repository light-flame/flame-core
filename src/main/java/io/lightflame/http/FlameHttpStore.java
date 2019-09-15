package io.lightflame.http;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.lightflame.routerules.HttpMethodRule;
import io.lightflame.routerules.HttpPathRule;
import io.lightflame.routerules.HttpPrefixPathRule;
import io.lightflame.routerules.RouteRules;
import io.lightflame.routerules.RouteStore;
import io.lightflame.routerules.Rule;
import io.lightflame.routerules.StoreKind;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;


/**
 * HttpBeanStore
 */
public class FlameHttpStore {

    static private Map<String, FlameHttpFunction> functionMap = new HashMap<>();

    private String prefix = "";

    public FlameHttpStore() {
    }

    public FlameHttpStore(String prefix) {
        this.prefix = prefix;
    }

    public FlameHttpContext runFunctionByRequest(FullHttpRequest request) throws Exception{
        FlameHttpFunction function = handler404();

        RouteRules routeRules = new RouteStore<FullHttpRequest>().getRouteRules(request, StoreKind.HTTP_STORE);
        if (routeRules != null) {
            function = functionMap.get(routeRules.getKey());
        }

        FlameHttpContext ctx = new FlameHttpContext(request, new HttpUtils(), routeRules);
        return function.chain(ctx);
    }

    public FlameHttpStore addHeaderRyle(){
        return this;
    }

    private String addRouteRule(Rule... rules){
        return new RouteStore<FullHttpRequest>().addRouteRule(
            new RouteRules(StoreKind.HTTP_STORE)
                .addRules(rules)
        );
    }

    public void httpGET(String url, FlameHttpFunction function){
        Rule pathRule = (url.contains("*")) ? new HttpPrefixPathRule(this.prefix + url) : new HttpPathRule(this.prefix + url);
        Rule methodRule = new HttpMethodRule(HttpMethod.GET);

        functionMap.put(this.addRouteRule(pathRule, methodRule), function);
    }


    public void httpPOST(String url, FlameHttpFunction function){
        Rule pathRule = (url.contains("*")) ? new HttpPrefixPathRule(this.prefix + url) : new HttpPathRule(this.prefix + url);
        Rule methodRule = new HttpMethodRule(HttpMethod.POST);

        functionMap.put(this.addRouteRule(pathRule, methodRule), function);
    }

    private FlameHttpFunction handler404() {
        return (ctx) -> {
            FullHttpResponse response = new DefaultFullHttpResponse(
                ctx.getRequest().protocolVersion(), 
                HttpResponseStatus.NOT_FOUND,
                Unpooled.wrappedBuffer("nothing here.. =(".getBytes()));
            response.headers()
                    .set(CONTENT_TYPE, TEXT_PLAIN)
                    .setInt(CONTENT_LENGTH, response.content().readableBytes());
            ctx.setResponse(response);
            return ctx;
        };
    }
    
}