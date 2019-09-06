package io.lightflame.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;


/**
 * HttpBeanStore
 */
public class DefaultHttpStore {

    static private Map<String, Function<FullHttpRequest, FullHttpResponse>> functionMap = new HashMap<>();

    private String prefix = "";

    public DefaultHttpStore() {
    }

    public DefaultHttpStore(String prefix) {
        this.prefix = prefix;
    }

    public Function<FullHttpRequest, FullHttpResponse> getFunctionByRequest(FullHttpRequest request){
        String key = String.format("%s|%s", 
            request.method().name(), 
            request.uri()
        ); 
        return functionMap.get(key);
    }

    public void httpGET(String url, Function<FullHttpRequest, FullHttpResponse> function){
        functionMap.put(String.format("GET|%s%s", prefix,url), function);
    }


    public void httpPOST(String url, Function<FullHttpRequest, FullHttpResponse> function){
        functionMap.put(String.format("POST|%s%s", prefix,url), function);
    }
    
}