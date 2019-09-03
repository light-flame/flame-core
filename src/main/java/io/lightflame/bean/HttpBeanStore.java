package io.lightflame.bean;

import java.util.HashMap;
import java.util.Map;

import io.lightflame.functions.HttpFunction;


/**
 * HttpBeanStore
 */
public class HttpBeanStore {

    static private Map<String, HttpFunction> functionMap = new HashMap<>();

    static public HttpFunction getFunction(String url){
        return functionMap.get(url);
    }

    public void addHttpFunction(String url, HttpFunction function){
        functionMap.put(url, function);
    }



    
}