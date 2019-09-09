package io.lightflame.store;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;


/**
 * HttpBeanStore
 */
public class FlameHttpStore {

    static private Map<HttpUrlScore, Function<FullHttpRequest, FullHttpResponse>> functionMap = new HashMap<>();

    private String prefix = "";

    public FlameHttpStore() {
    }

    public FlameHttpStore(String prefix) {
        this.prefix = prefix;
    }

    public Function<FullHttpRequest, FullHttpResponse> getFunctionByRequest(FullHttpRequest request){
        Function<FullHttpRequest, FullHttpResponse> function = handler404();
        // for each iterator

        return function;
    }

    public void httpGET(String url, Function<FullHttpRequest, FullHttpResponse> function){
        
        functionMap.put(new HttpUrlScore(this.prefix + url, "GET"), function);
    }


    public void httpPOST(String url, Function<FullHttpRequest, FullHttpResponse> function){
        functionMap.put(new HttpUrlScore(this.prefix + url, "GET"), function);
    }

    private Function<FullHttpRequest, FullHttpResponse> handler404() {
        return (req) -> {
            FullHttpResponse response = new DefaultFullHttpResponse(
                req.protocolVersion(), 
                OK,
                Unpooled.wrappedBuffer("nothing here.. =(".getBytes()));
            response.headers()
                    .set(CONTENT_TYPE, TEXT_PLAIN)
                    .setInt(CONTENT_LENGTH, response.content().readableBytes());
            return response;
        };
    }
    
}