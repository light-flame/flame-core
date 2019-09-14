package io.lightflame.http;

import io.lightflame.http.HttpRouteRules.HttpRouteRule;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * FlameHttpContext
 */
public class FlameHttpContext{

    private FullHttpRequest req;
    private HttpRouteRule routeRule;
    private FullHttpResponse res;
    private HttpUtils utils;

    public String getPathParam(String name){
        return utils.extractUrlParam(req.uri(), name, routeRule);
    }

    public String getPathWithoutPrefix(){
        return utils.getPathWithoutPrefix(req.uri(), routeRule);
    }

    public String getQueryUrl(String name){
        return utils.extractQueryParam(req.uri(), name);
    }

    public FullHttpRequest getRequest(){
        return this.req;
    }

    public FlameHttpContext setResponse(FullHttpResponse r){
        this.res = r;
        return this;
    }

    public FullHttpResponse getResponse(){
        return res;
    }

    public FlameHttpContext(FullHttpRequest req) {
        this.req = req;
    }
    
    public FlameHttpContext(FullHttpRequest req, HttpUtils utils, HttpRouteRule routeRule) {
        this.routeRule = routeRule;
        this.utils = utils;
        this.req = req;
    }
}