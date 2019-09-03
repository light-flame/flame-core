package io.lightflame.bean;

import java.util.HashMap;
import java.util.Map;

import io.lightflame.functions.DefaultHttpFunction;
import io.lightflame.http.HTTPRequest;


/**
 * HttpBeanStore
 */
public class DefaultHttpStore {

    static private Map<String, DefaultHttpFunction> functionMap = new HashMap<>();

    private String prefix = "";

    public DefaultHttpStore() {
    }

    public DefaultHttpStore(String prefix) {
        this.prefix = prefix;
    }

    public DefaultHttpFunction getFunctionByRequest(HTTPRequest request){
        String key = String.format("%s|%s", 
            request.getMethod().getHttpMethod(), 
            request.getLocation()
        ); 
        return functionMap.get(key);
    }

    public void httpGET(String url, DefaultHttpFunction function){
        functionMap.put(String.format("GET|%s%s", prefix,url), function);
    }


    public void httpPOST(String url, DefaultHttpFunction function){
        functionMap.put(String.format("POST|%s%s", prefix,url), function);
    }
    
}