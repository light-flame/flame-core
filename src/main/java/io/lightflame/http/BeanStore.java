package io.lightflame.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle
 */
class BeanStore {
    
    static private Map<String, Bean> beanMap = new HashMap<>();

    static public Bean getBean(String key){
        return beanMap.get(key);
    }

    static public Bean addBean(String key, Bean bean){
        return beanMap.put(key, bean);
    }
    
}