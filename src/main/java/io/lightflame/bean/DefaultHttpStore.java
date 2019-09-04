package io.lightflame.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.lightflame.http.HTTPRequest;
import io.lightflame.http.HTTPResponse;
import io.lightflame.http.HTTPSession;


/**
 * HttpBeanStore
 */
public class DefaultHttpStore {

    static private Map<String, Function<HTTPSession, HTTPResponse>> functionMap = new HashMap<>();

    private String prefix = "";

    public DefaultHttpStore() {
    }

    public DefaultHttpStore(String prefix) {
        this.prefix = prefix;
    }

    public Function<HTTPSession, HTTPResponse> getFunctionByRequest(HTTPRequest request){
        String key = String.format("%s|%s", 
            request.getMethod().getHttpMethod(), 
            request.getLocation()
        ); 
        return functionMap.get(key);
    }

    public void httpGET(String url, Function<HTTPSession, HTTPResponse> function){
        functionMap.put(String.format("GET|%s%s", prefix,url), function);
    }


    public void httpPOST(String url, Function<HTTPSession, HTTPResponse> function){
        functionMap.put(String.format("POST|%s%s", prefix,url), function);
    }
    
}