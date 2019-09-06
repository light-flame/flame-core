package io.lightflame.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;


/**
 * HttpBeanStore
 */
public class NettyHttpStore {

    static private Map<String, Function<HttpRequest, FullHttpResponse>> functionMap = new HashMap<>();

    private String prefix = "";

    public NettyHttpStore() {
    }

    public NettyHttpStore(String prefix) {
        this.prefix = prefix;
    }

    public Function<HttpRequest, FullHttpResponse> getFunctionByRequest(HttpRequest request){
        String key = String.format("%s|%s", 
            request.method().name(), 
            request.uri()
        ); 
        return functionMap.get(key);
    }

    public void httpGET(String url, Function<HttpRequest, FullHttpResponse> function){
        functionMap.put(String.format("GET|%s%s", prefix,url), function);
    }


    public void httpPOST(String url, Function<HttpRequest, FullHttpResponse> function){
        functionMap.put(String.format("POST|%s%s", prefix,url), function);
    }
    
}