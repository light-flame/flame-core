package io.lightflame.context;

import io.lightflame.util.FlameHttpUtils;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * FlameHttpContext
 */
public class FlameHttpContext{

    private FullHttpRequest req;
    private FullHttpResponse res;
    private FlameHttpUtils utils;

    public String getPathParam(String name){
        return utils.extractUrlParam(req.uri(), name);
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
    
    public FlameHttpContext(FullHttpRequest req, FlameHttpUtils utils) {
        this.utils = utils;
        this.req = req;
    }
}