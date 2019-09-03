package io.lightflame.bean;

import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

import io.lightflame.annotations.Endpoint;
import io.lightflame.annotations.Handler;
import io.lightflame.functions.HttpFunction;
import io.lightflame.http.HTTPRequest;


/**
 * HttpBeanStore
 */
public class HttpBeanStore {

    static private Map<String, HttpFunction> functionMap = new HashMap<>();

    private String prefix = "";

    public HttpBeanStore() {
    }

    public HttpBeanStore(String prefix) {
        this.prefix = prefix;
    }

    public HttpFunction getFunctionByRequest(HTTPRequest request){
        String key = String.format("%s|%s", 
            request.getMethod().getHttpMethod(), 
            request.getLocation()
        ); 
        return functionMap.get(key);
    }

    public void httpGET(String url, HttpFunction function){
        functionMap.put(String.format("GET|%s%s", prefix,url), function);
    }


    public void httpPOST(String url, HttpFunction function){
        functionMap.put(String.format("POST|%s%s", prefix,url), function);
    }
    
}