package io.lightflame.context;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * FlameHttpContext
 */
public class FlameHttpContext implements FlameContext{

    private FullHttpRequest req;
    private FullHttpResponse res;

    

    public FullHttpRequest request(){
        return this.req;
    }

    public void setResponse(FullHttpResponse r){
        this.res = r;
    }

    public FullHttpResponse getResponse(){
        return res;
    }

    public FlameHttpContext(FullHttpRequest req) {
        this.req = req;
    }
    
}