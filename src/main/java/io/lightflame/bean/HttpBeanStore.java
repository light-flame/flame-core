package io.lightflame.bean;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpBeanStore
 */
public class HttpBeanStore {

    // url - clazz
    static private Map<String,Class<?>> mapUrlClazz = new HashMap<>();

    static public Bean<?> getBean(Class<?> clazz){
        return BeanStore.getBean(clazz);
    }

    static public Bean<?> getBeanByURL(String url){
        Class<?> clazz =  mapUrlClazz.get(url);
        return getBean(clazz);
    }

    static public void addHttpBean(Bean<?> bean){
        BeanStore.addBean(bean);
    }

    static public void addBeanMethod(Class<?> clazz, String url, Method method){
        Bean<?> bean = getBean(clazz);
        bean.addMethod(url, method);
        mapUrlClazz.put(url, clazz);
    }

    
}